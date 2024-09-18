package ru.practicum.mainservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.StatDto;
import ru.practicum.mainservice.exception.BadRequestValidationException;
import ru.practicum.mainservice.exception.ConflictValidationException;
import ru.practicum.mainservice.mappers.EventMapper;
import ru.practicum.mainservice.mappers.LocationMapper;
import ru.practicum.mainservice.mappers.UserMapper;
import ru.practicum.mainservice.model.Constants;
import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.model.enums.AdminStateAction;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.enums.SortType;
import ru.practicum.mainservice.model.enums.UserStateAction;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.event.Location;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.EventShortDto;
import ru.practicum.mainservice.model.event.dto.NewEventDto;
import ru.practicum.mainservice.model.event.dto.UpdateEventRequest;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.searchparams.PresentationParameters;
import ru.practicum.mainservice.searchparams.SearchParametersAdmin;
import ru.practicum.mainservice.searchparams.SearchParametersUsersPublic;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final StatClient statsServerClient2;
    private final ObjectMapper objectMapper;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Transactional
    public EventFullDto save(int userId, NewEventDto newEventDto) {

        if (LocalDateTime.parse(newEventDto.getEventDate(), Constants.DATE_TIME_FORMAT)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestValidationException("Дата события не может быть раньше, чем через два часа от текущего момента");
        }
        User initiator = userService.getUser(userId);
        Category category = categoryService.getCategory(newEventDto.getCategory());
        Location location = locationService.saveLocation(LocationMapper.toLocation(newEventDto.getLocation()));
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(newEventDto, initiator, category, location)));
    }

    @Transactional(readOnly = true)
    public List<Event> getEventsByIds(List<Integer> eventIds) {
        return eventRepository.findAllById(eventIds);
    }

    @Transactional
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Transactional
    public EventFullDto updateByAdmin(int eventId, UpdateEventRequest updateEventRequest) {
        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryService.getCategory(updateEventRequest.getCategory());
        }
        Event updatingEvent = getEvent(eventId);
        if (updateEventRequest.getStateAction() != null) {
            AdminStateAction adminStateAction = AdminStateAction.from(updateEventRequest.getStateAction()).orElseThrow(() ->
                    new IllegalArgumentException("Unknown state: " + updateEventRequest.getStateAction()));
            if (adminStateAction.equals(AdminStateAction.PUBLISH_EVENT)) {
                if (!updatingEvent.getState().equals(EventState.PENDING)) {
                    throw new ConflictValidationException("Можно публиковать только события в ожидании публикации");
                }
                updatingEvent.setState(EventState.PUBLISHED);
            } else {
                if (!updatingEvent.getState().equals(EventState.PENDING)) {
                    throw new ConflictValidationException("Можно отклонить только события в ожидании публикации");
                }
                updatingEvent.setState(EventState.CANCELED);
            }
        }
        return update(updatingEvent, updateEventRequest, category, Constants.UPDATE_TIME_LIMIT_ADMIN);
    }

    @Transactional
    public EventFullDto updateByUser(int userId, int eventId, UpdateEventRequest updateEventRequest) {
        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryService.getCategory(updateEventRequest.getCategory());
        }
        Event updatingEvent = getEvent(eventId);
        if (updatingEvent.getInitiator().getId() != userId) {
            throw new BadRequestValidationException("Событие может редактировать только инициатор");
        }
        if (updatingEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictValidationException("Нельзя изменить опубликованное событие");
        }
        if (updateEventRequest.getStateAction() != null) {
            UserStateAction userStateAction = UserStateAction.from(updateEventRequest.getStateAction()).orElseThrow(() ->
                    new IllegalArgumentException("Unknown state: " + updateEventRequest.getStateAction()));
            if (userStateAction.equals(UserStateAction.SEND_TO_REVIEW)) {
                updatingEvent.setState(EventState.PENDING);
            } else {
                updatingEvent.setState(EventState.CANCELED);
            }
        }
        return update(updatingEvent, updateEventRequest, category, Constants.UPDATE_TIME_LIMIT_USER);
    }

    /**
     * Вызывается методами EventService, к которым уже применяется Transactional
     */
    private EventFullDto update(Event updatingEvent, UpdateEventRequest updateEventRequest, Category category, int timeLimit) {
        if (updateEventRequest.getAnnotation() != null) {
            updatingEvent.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            updatingEvent.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            updatingEvent.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventRequest.getEventDate(), Constants.DATE_TIME_FORMAT)
                    .isBefore(LocalDateTime.now().plusHours(timeLimit))) {
                throw new BadRequestValidationException("Дата события не может быть раньше, чем через "
                        + timeLimit + " ч. от текущего момента");
            }
            updatingEvent.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), Constants.DATE_TIME_FORMAT));
        }
        if (updateEventRequest.getLocation() != null) {
            Location locationForUpdate = LocationMapper.toLocation(updateEventRequest.getLocation());
            locationForUpdate.setId(updatingEvent.getLocation().getId());
            locationService.saveLocation(locationForUpdate);
            updatingEvent.setLocation(locationForUpdate);
        }
        if (updateEventRequest.getPaid() != null) {
            updatingEvent.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            updatingEvent.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            updatingEvent.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            updatingEvent.setTitle(updateEventRequest.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(updatingEvent));
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        userService.getUser(userId).getId();
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Event> events;
        events = eventRepository.findAllByInitiator_Id(userId, pageable);
        List<EventFullDto> fullEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        return addStatsToEventFullDtoInformation(fullEventsDtoNoViews).stream()
                .map(EventMapper::fromFullToShortEventDTO)
                .collect(Collectors.toList());
    }

    /**
     * Вызывается методами EventService, к которым уже применяется Transactional
     */
    private List<EventFullDto> addStatsToEventFullDtoInformation(List<EventFullDto> eventsFullDto) {
        List<String> urisInList = new ArrayList<>();
        for (EventFullDto event : eventsFullDto) {
            urisInList.add("/events/" + event.getId());
        }
        String[] uris = urisInList.toArray(new String[0]);
        Map<Integer, Integer> statistic = getHitsStatistic(uris);
        for (EventFullDto eventFullDto : eventsFullDto) {
            if (statistic.get(eventFullDto.getId()) != null) {
                eventFullDto.setViews(statistic.get(eventFullDto.getId()));
            }
        }
        return eventsFullDto;
    }

    /**
     * Вызывается методами EventService, к которым уже применяется Transactional
     */
    private Map<Integer, Integer> getHitsStatistic(String[] uris) {
        String start = LocalDateTime.now().minusYears(Constants.FREE_TIME_INTERVAL).format(Constants.DATE_TIME_FORMAT);
        String end = LocalDateTime.now().plusYears(Constants.FREE_TIME_INTERVAL).format(Constants.DATE_TIME_FORMAT);
        List<StatDto> stats = statsServerClient2.getStat(start, end, Arrays.stream(uris).toList(), true);
        //ResponseEntity<Object> response = statsServerClient.getHitsStatistics(start, end, uris, true);
        System.out.println("Получил в ответ: " + stats);
        Map<Integer, Integer> statistic = new HashMap<>();
        if (!stats.isEmpty()) {
            for (StatDto statsDtoOut : stats) {
                if (statsDtoOut.getUri().length() > "/events".length()) {
                    statistic.put(Integer.parseInt(statsDtoOut.getUri().substring("/events/".length())),
                            Integer.parseInt(String.valueOf(statsDtoOut.getHits())));
                }
            }
        }

        return statistic;
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsWithFilteringForPublic(SearchParametersUsersPublic searchParametersUsersPublic,
                                                               PresentationParameters presentationParameters,
                                                               HttpServletRequest servletRequest) {
        log.info("вызываем клиент статистики");
        statsServerClient2.saveStat(new HitRequestDto(
                "emw-main-service",
                "/events",
                servletRequest.getRemoteAddr(),
                LocalDateTime.now().format(Constants.DATE_TIME_FORMAT)));
        log.info("вызвали клиент статистики");
        Pageable pageable = PageRequest.of(presentationParameters.getFrom() / presentationParameters.getSize(),
                presentationParameters.getSize());
        String text = null;
        if (searchParametersUsersPublic.getText() != null) {
            text = searchParametersUsersPublic.getText().toLowerCase();
        }
        List<Integer> categories = null;
        if (searchParametersUsersPublic.getCategories() != null) {
            categories = searchParametersUsersPublic.getCategories();
        }
        Boolean paid = null;
        if (searchParametersUsersPublic.getPaid() != null) {
            paid = searchParametersUsersPublic.getPaid();
        }
        LocalDateTime rangeStart = LocalDateTime.now().minusYears(Constants.FREE_TIME_INTERVAL);
        LocalDateTime rangeEnd = LocalDateTime.now().plusYears(Constants.FREE_TIME_INTERVAL);
        if (searchParametersUsersPublic.getRangeStart() == null && searchParametersUsersPublic.getRangeEnd() == null) {
            rangeStart = LocalDateTime.now();
        } else if (searchParametersUsersPublic.getRangeStart() != null && searchParametersUsersPublic.getRangeEnd() == null) {
            rangeStart = searchParametersUsersPublic.getRangeStart();
        } else if (searchParametersUsersPublic.getRangeStart() == null) {
            rangeEnd = searchParametersUsersPublic.getRangeEnd();
        } else {
            rangeStart = searchParametersUsersPublic.getRangeStart();
            rangeEnd = searchParametersUsersPublic.getRangeEnd();
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestValidationException("Переданы некорректные даты начала и окончания диапазона поиска");
            }
        }
        Page<Event> events;
        events = eventRepository.findByParametersForPublic(EventState.PUBLISHED, text, categories, paid, rangeStart,
                rangeEnd, pageable);
        List<EventFullDto> fullEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        List<EventFullDto> fullEventsDtoWithViews = addStatsToEventFullDtoInformation(fullEventsDtoNoViews);
        List<EventFullDto> filteredByParticipationLimit = new ArrayList<>();
        for (EventFullDto eventFullDto : fullEventsDtoWithViews) {
            if (eventFullDto.getParticipantLimit() == 0 || eventFullDto.getConfirmedRequests()
                    < eventFullDto.getParticipantLimit()) {
                filteredByParticipationLimit.add(eventFullDto);
            }
        }
        if (presentationParameters.getSort() == SortType.VIEWS) {
            filteredByParticipationLimit = filteredByParticipationLimit.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews).reversed())
                    .collect(Collectors.toList());
        }
        return filteredByParticipationLimit.stream()
                .map(EventMapper::fromFullToShortEventDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventFullDto getEventForPublic(int id, HttpServletRequest servletRequest) {
        Event event = getEvent(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NoSuchElementException("Событие с id " + id + " не опубликовано");
        }
        List<String> urisInList = new ArrayList<>();
        urisInList.add("/events/" + id);
        String[] uris = urisInList.toArray(new String[0]);
        Map<Integer, Integer> statistic = getHitsStatistic(uris);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        if (statistic.get(eventFullDto.getId()) != null) {
            eventFullDto.setViews(statistic.get(eventFullDto.getId()));
        }
        statsServerClient2.saveStat(new HitRequestDto(
                "emw-main-service",
                "/events/" + id,
                servletRequest.getRemoteAddr(),
                LocalDateTime.now().format(Constants.DATE_TIME_FORMAT)));
        return eventFullDto;
    }

    @Transactional(readOnly = true)
    public EventFullDto getEventOfUserForPrivate(int userId, int eventId) {
        User user = userService.getUser(userId);
        UserMapper.toUserDto(user);
        Event event = getEvent(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ConflictValidationException("Нельзя просматривать чужие события");
        }
        List<String> urisInList = new ArrayList<>();
        urisInList.add("/events/" + eventId);
        String[] uris = urisInList.toArray(new String[0]);
        Map<Integer, Integer> statistic = getHitsStatistic(uris);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        if (statistic.get(eventFullDto.getId()) != null) {
            eventFullDto.setViews(statistic.get(eventFullDto.getId()));
        }
        return eventFullDto;
    }

    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsWithFilteringForAdmin(SearchParametersAdmin searchParametersAdmin,
                                                             PresentationParameters presentationParameters) {
        Pageable pageable = PageRequest.of(presentationParameters.getFrom() / presentationParameters.getSize(),
                presentationParameters.getSize());
        List<Integer> users = null;
        if (searchParametersAdmin.getUsers() != null) {
            users = searchParametersAdmin.getUsers();
        }
        List<EventState> states = null;
        if (searchParametersAdmin.getStates() != null) {
            states = searchParametersAdmin.getStates();
        }
        List<Integer> categories = null;
        if (searchParametersAdmin.getCategories() != null) {
            categories = searchParametersAdmin.getCategories();
        }
        LocalDateTime rangeStart = LocalDateTime.now().minusYears(Constants.FREE_TIME_INTERVAL);
        LocalDateTime rangeEnd = LocalDateTime.now().plusYears(Constants.FREE_TIME_INTERVAL);
        if (searchParametersAdmin.getRangeStart() == null && searchParametersAdmin.getRangeEnd() == null) {
            rangeStart = LocalDateTime.now();
        } else if (searchParametersAdmin.getRangeStart() != null && searchParametersAdmin.getRangeEnd() == null) {
            rangeStart = searchParametersAdmin.getRangeStart();
        } else if (searchParametersAdmin.getRangeStart() == null) {
            rangeEnd = searchParametersAdmin.getRangeEnd();
        } else {
            rangeStart = searchParametersAdmin.getRangeStart();
            rangeEnd = searchParametersAdmin.getRangeEnd();
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestValidationException("Переданы некорректные даты начала и окончания диапазона поиска");
            }
        }
        Page<Event> events;
        events = eventRepository.findByParametersForAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        List<EventFullDto> fullEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        return addStatsToEventFullDtoInformation(fullEventsDtoNoViews);
    }

    @Transactional(readOnly = true)
    public Event getEvent(int eventId) {
        return eventRepository.getReferenceById(eventId);
    }
}
