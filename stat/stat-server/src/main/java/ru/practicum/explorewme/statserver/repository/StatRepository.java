package ru.practicum.explorewme.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewme.statserver.model.Hit;

public interface StatRepository extends JpaRepository<Long, Hit> {

}
