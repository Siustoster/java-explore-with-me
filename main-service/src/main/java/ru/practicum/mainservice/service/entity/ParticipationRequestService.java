package ru.practicum.mainservice.service.entity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.BadRequestValidationException;
import ru.practicum.mainservice.mappers.ParticipationRequestMapper;
import ru.practicum.mainservice.model.enums.ParticipationRequestStatus;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.request.ParticipationRequest;
import ru.practicum.mainservice.model.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;

    @Transactional
    public ParticipationRequestDto save(User requester, Event event, ParticipationRequestStatus participationRequestStatus) {
        ParticipationRequest participationRequest = new ParticipationRequest(
                0,
                LocalDateTime.now(),
                event, requester,
                participationRequestStatus
        );
        log.info("Сохраняется запрос с параметрами: eventId={}, requesterId={}, status={}", event.getId(), requester.getId(), participationRequestStatus);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Transactional
    public void saveAll(List<ParticipationRequest> updatedParticipationRequests) {
        participationRequestRepository.saveAll(updatedParticipationRequests);
    }

    @Transactional
    public ParticipationRequestDto cancel(int userId, int requestId) {
        ParticipationRequest canceledParticipationRequest = participationRequestRepository.getReferenceById(requestId);
        if (canceledParticipationRequest.getRequester().getId() != userId) {
            throw new BadRequestValidationException("Нельзя отменять чужие запросы");
        }
        canceledParticipationRequest.setStatus(ParticipationRequestStatus.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(canceledParticipationRequest));
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(int userId) {
        return participationRequestRepository.findAllByRequester_Id(userId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(int eventId) {
        return participationRequestRepository.findAllByEvent_Id(eventId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequest> getRequestByIds(List<Integer> requestIds) {
        return participationRequestRepository.findAllByIdIn(requestIds);
    }

    @Transactional(readOnly = true)
    public ParticipationRequest getRequestByEventAndRequester(int eventId, int requesterId) {
        return participationRequestRepository.findOneByEvent_IdAndRequester_Id(eventId,requesterId);
    }
}
