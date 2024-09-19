package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.BadRequestValidationException;
import ru.practicum.mainservice.model.Constants;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.request.ParticipationRequest;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.repository.RateRepository;
import ru.practicum.mainservice.model.rate.Rate;
import ru.practicum.mainservice.model.enums.ParticipationRequestStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final EventService eventService;
    private final UserService userService;
    private final ParticipationRequestService participationRequestService;

    @Transactional
    public void putMark(int userId, int eventId, Boolean score) {
        Rate mark = rateRepository.findOneByEvaluator_IdAndEvent_Id(userId, eventId);
        Event event = eventService.getEvent(eventId);
        User initiator = userService.getUser(event.getInitiator().getId());
        if (mark != null) {
            if (!mark.getScore().toString().equals(score.toString())) {
                if (mark.getScore()) {
                    event.setRating(event.getRating() - Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                    initiator.setRating(initiator.getRating() - Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                } else {
                    event.setRating(event.getRating() + Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                    initiator.setRating(initiator.getRating() + Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                }
                mark.setScore(score);
            }
        } else {
            ParticipationRequest request = participationRequestService.getRequestByEventAndRequester(eventId, userId);
            if (request == null) {
                throw new BadRequestValidationException("Оценивать событие могут только его участники");
            }
            if (event.getParticipantLimit() == 0 || !event.getRequestModeration() || request.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                User evaluator = userService.getUser(userId);
                mark = new Rate(0, evaluator, event, score);
                rateRepository.save(mark);
                if (score) {
                    event.setRating(event.getRating() + Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                    initiator.setRating(initiator.getRating() + Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                } else {
                    event.setRating(event.getRating() - Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                    initiator.setRating(initiator.getRating() - Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                }
            } else {
                throw new BadRequestValidationException("Оценивать событие могут только его участники");
            }
        }
        eventService.save(event);
        rateRepository.save(mark);
        userService.saveUser(initiator);
    }

    @Transactional
    public void deleteMark(int userId, int eventId) {
        Rate mark = rateRepository.findOneByEvaluator_IdAndEvent_Id(userId, eventId);
        if (mark == null) {
            return;
        }
        Event event = eventService.getEvent(eventId);
        User initiator = userService.getUser(event.getInitiator().getId());
        if (mark.getScore()) {
            event.setRating(event.getRating() - Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
            initiator.setRating(initiator.getRating() - Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
        } else {
            event.setRating(event.getRating() + Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
            initiator.setRating(initiator.getRating() + Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
        }
        eventService.save(event);
        userService.saveUser(initiator);
        rateRepository.delete(mark);

    }
}
