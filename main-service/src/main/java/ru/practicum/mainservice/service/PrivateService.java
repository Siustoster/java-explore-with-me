package ru.practicum.mainservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.BadRequestValidationException;
import ru.practicum.mainservice.exception.ConflictValidationException;
import ru.practicum.mainservice.mappers.ParticipationRequestMapper;
import ru.practicum.mainservice.mappers.UserMapper;
import ru.practicum.mainservice.model.Constants;
import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.enums.ParticipationRequestStatus;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.event.dto.EventFullDto;
import ru.practicum.mainservice.model.event.dto.EventShortDto;
import ru.practicum.mainservice.model.event.dto.NewEventDto;
import ru.practicum.mainservice.model.event.dto.UpdateEventRequest;
import ru.practicum.mainservice.model.request.ParticipationRequest;
import ru.practicum.mainservice.model.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.model.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.model.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.service.entity.CategoryService;
import ru.practicum.mainservice.service.entity.EventService;
import ru.practicum.mainservice.service.entity.ParticipationRequestService;
import ru.practicum.mainservice.service.entity.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PrivateService {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestService participationRequestService;
    //private final MarkService markService;

    /**
     * События
     */

    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        userService.getUser(userId).getId();
        return eventService.getUserEvents(userId, from, size);
    }

    @Transactional
    public EventFullDto saveEvent(int userId, NewEventDto newEventDto) {
        if (LocalDateTime.parse(newEventDto.getEventDate(), Constants.DATE_TIME_FORMAT)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestValidationException("Дата события не может быть раньше, чем через два часа от текущего момента");
        }
        User initiator = userService.getUser(userId);
        Category category = categoryService.getCategory(newEventDto.getCategory());
        return eventService.save(newEventDto, initiator, category);
    }

    @Transactional(readOnly = true)
    public EventFullDto getEventOfUser(int userId, int eventId) {
        User user = userService.getUser(userId);
        UserMapper.toUserDto(user);
        return eventService.getEventOfUserForPrivate(userId, eventId);
    }

    @Transactional
    public EventFullDto updateEventOfUser(int userId, int eventId, UpdateEventRequest updateEventRequest) {
        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryService.getCategory(updateEventRequest.getCategory());
        }
        return eventService.updateByUser(userId, eventId, updateEventRequest, category);
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(int userId, int eventId) {
        int initiatorId = eventService.getEvent(eventId).getInitiator().getId();
        if (userId != initiatorId) {
            throw new BadRequestValidationException("Событий пользователя не найдено");
        }
        return participationRequestService.getRequestsForParticipationInUserEvent(eventId);
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(int userId, int eventId,
                                                               EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userService.getUser(userId).getId();
        Event event = eventService.getEvent(eventId);
        List<Integer> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<ParticipationRequest> participationRequests = participationRequestService.getRequestByIds(requestIds);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + participationRequests.size());
            eventService.save(event);
            List<ParticipationRequestDto> participationRequestsDto = participationRequests.stream()
                    .map(ParticipationRequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            return new EventRequestStatusUpdateResult(participationRequestsDto, new ArrayList<>());
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictValidationException("Достигнут предел количества участников. Заявки отклонены");
        }
        List<ParticipationRequest> updatedParticipationRequests = new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequest participationRequest : participationRequests) {
            if (!participationRequest.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new ConflictValidationException("Статус заявок можно изменить только у " +
                        "находящихся в режиме ожидания. Заявки отклонены");
            }
            if (event.getParticipantLimit() > event.getConfirmedRequests() &&
                    ParticipationRequestStatus.from(eventRequestStatusUpdateRequest.getStatus()).orElseThrow(() ->
                                    new IllegalArgumentException("Unknown state: " + eventRequestStatusUpdateRequest.getStatus()))
                            .equals(ParticipationRequestStatus.CONFIRMED)) {
                participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
                updatedParticipationRequests.add(participationRequest);
                confirmedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                participationRequest.setStatus(ParticipationRequestStatus.REJECTED);
                updatedParticipationRequests.add(participationRequest);
                rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
            }
        }
        eventService.save(event);
        participationRequestService.saveAll(updatedParticipationRequests);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    /**
     * Запросы на участие
     */

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(int userId) {
        userService.getUser(userId).getId();
        return participationRequestService.getUserRequests(userId);
    }

    @Transactional
    public ParticipationRequestDto saveParticipationRequest(int requesterId, int eventId) {
        User requester = userService.getUser(requesterId);
        Event event = eventService.getEvent(eventId);
        if (requesterId == event.getInitiator().getId()) {
            throw new ConflictValidationException("Инициатор не может делать запрос на участие в своём событии");
        }
        ParticipationRequest participationRequest = participationRequestService.getRequestByEventAndRequester(eventId, requesterId);
        if (participationRequest != null) {
            throw new ConflictValidationException("Повторный запрос подать нельзя");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictValidationException("Событие не опубликовано");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictValidationException("Достигнут предел количества участников");
        }
        ParticipationRequestStatus participationRequestStatus = ParticipationRequestStatus.PENDING;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequestStatus = ParticipationRequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.save(event);
        }
        return participationRequestService.save(requester, event, participationRequestStatus);
    }

    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(int userId, int requestId) {
        return participationRequestService.cancel(userId, requestId);
    }
}
