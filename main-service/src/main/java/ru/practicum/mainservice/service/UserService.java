package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.BadRequestValidationException;
import ru.practicum.mainservice.exception.ConflictValidationException;
import ru.practicum.mainservice.mappers.UserMapper;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.model.user.dto.UserDto;
import ru.practicum.mainservice.model.user.dto.UserDtoWithRating;
import ru.practicum.mainservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto save(UserDto userDto) {
        if ((userDto.getName() == null) || (userDto.getName().isBlank()) || userDto.getEmail() == null
                || userDto.getEmail().isBlank()) {
            throw new BadRequestValidationException("Переданы некорректные данные для создания user");
        }
        if (!userRepository.findAllByEmail(userDto.getEmail()).isEmpty()) {
            throw new ConflictValidationException("Имейл уже зарегистрирован");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<User> users;
        users = userRepository.findUsersForAdmin(ids, pageable);
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(int userId) {
        User user = getUser(userId);
        user.getId();
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public User getUser(int userId) {
        return userRepository.getReferenceById(userId);
    }

    @Transactional(readOnly = true)
    public List<UserDtoWithRating> getUsersWithRating(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<User> users;
        users = userRepository.findAllWhereRatingNotEqualToZeroSortByRating(pageable);
        return users.stream()
                .map(UserMapper::toUserDtoWithRating)
                .collect(Collectors.toList());
    }
}
