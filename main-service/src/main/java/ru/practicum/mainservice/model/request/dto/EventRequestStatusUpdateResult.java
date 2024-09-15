package ru.practicum.mainservice.model.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    protected List<ParticipationRequestDto> confirmedRequests;
    protected List<ParticipationRequestDto> rejectedRequests;
}
