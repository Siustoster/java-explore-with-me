package ru.practicum.mainservice.model.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDtoWithRating {
    private int id;
    private String name;
    private String email;
    private int rating;
}