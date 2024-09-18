package ru.practicum.mainservice.mappers;

import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.model.user.dto.UserDto;
import ru.practicum.mainservice.model.user.dto.UserShortDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }


    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }


    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId() != null ? userDto.getId() : 0,
                userDto.getName() != null ? userDto.getName() : "",
                userDto.getEmail() != null ? (userDto.getEmail()) : ""
        );
    }
}
