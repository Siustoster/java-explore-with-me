package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.model.user.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User AS u " +
            "WHERE (u.id IN :ids OR :ids IS NULL)")
    Page<User> findUsersForAdmin(@Param("ids") List<Integer> ids, Pageable pageable);

    List<User> findAllByEmail(String email);
}
