package ru.practicum.mainservice.model.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    protected Integer id;
    @Positive
    protected int requester;
    protected String created;
    @Positive
    protected int event;
    @NotBlank
    protected String status;
}
