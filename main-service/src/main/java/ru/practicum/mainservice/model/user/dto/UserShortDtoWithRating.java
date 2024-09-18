package ru.practicum.mainservice.model.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDtoWithRating {
    private int id;
    private String name;
    private int rating;
}