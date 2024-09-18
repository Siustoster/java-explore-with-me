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
import ru.practicum.mainservice.repository.MarkRepository;
import ru.practicum.mainservice.model.mark.Mark;
import ru.practicum.mainservice.model.enums.ParticipationRequestStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkRepository markRepository;
    private final EventService eventService;
    private final UserService userService;
    private final ParticipationRequestService participationRequestService;

    @Transactional
    public void saveMark(Mark mark) {
        markRepository.save(mark);
    }

    @Transactional
    public void deleteMark(Mark mark) {
        markRepository.delete(mark);
    }

    @Transactional(readOnly = true)
    public Mark findMarkByEvaluatorAndEvent(int evaluatorId, int eventId) {
        return markRepository.findOneByEvaluator_IdAndEvent_Id(evaluatorId, eventId);
    }

    @Transactional
    public void putMark(int userId, int eventId, Boolean score) {
        Mark mark = markRepository.findOneByEvaluator_IdAndEvent_Id(userId, eventId);
        ;
        if (mark != null) {
            Event event = eventService.getEvent(eventId);
            User initiator = userService.getUser(event.getInitiator().getId());
            if (!mark.getScore().toString().equals(score.toString())) {
                if (mark.getScore()) {
                    event.setRating(event.getRating() - Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                    initiator.setRating(initiator.getRating() - Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                } else {
                    event.setRating(event.getRating() + Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                    initiator.setRating(initiator.getRating() + Constants.CHANGING_RATING_WHEN_CHANGING_MARK);
                }
                mark.setScore(score);
                eventService.save(event);
                markRepository.save(mark);
                userService.saveUser(initiator);
            }
        } else {
            ParticipationRequest request = participationRequestService.getRequestByEventAndRequester(eventId, userId);
            if (request == null) {
                throw new BadRequestValidationException("Оценивать событие могут только его участники");
            }
            Event event = eventService.getEvent(eventId);
            if (event.getParticipantLimit() == 0 || !event.getRequestModeration() || request.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                User evaluator = userService.getUser(userId);
                mark = new Mark(0, evaluator, event, score);
                markRepository.save(mark);
                User initiator = userService.getUser(event.getInitiator().getId());
                if (score) {
                    event.setRating(event.getRating() + Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                    initiator.setRating(initiator.getRating() + Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                } else {
                    event.setRating(event.getRating() - Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                    initiator.setRating(initiator.getRating() - Constants.RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK);
                }
                eventService.save(event);
                markRepository.save(mark);
                userService.saveUser(initiator);
            } else {
                throw new BadRequestValidationException("Оценивать событие могут только его участники");
            }
        }
    }

    @Transactional
    public void deleteMark(int userId, int eventId) {
        Mark mark = markRepository.findOneByEvaluator_IdAndEvent_Id(userId, eventId);
        ;
        if (mark != null) {
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
            markRepository.delete(mark);
        }
    }
}
