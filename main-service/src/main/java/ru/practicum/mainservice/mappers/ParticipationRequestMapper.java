package ru.practicum.mainservice.mappers;

import ru.practicum.mainservice.model.request.ParticipationRequest;
import ru.practicum.mainservice.model.request.dto.ParticipationRequestDto;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getCreated().toString(),
                participationRequest.getEvent().getId(),
                participationRequest.getStatus().toString()
        );
    }
}
