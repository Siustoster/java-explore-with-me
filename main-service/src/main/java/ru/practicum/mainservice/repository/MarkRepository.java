package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.mark.Mark;

public interface MarkRepository extends JpaRepository<Mark, Integer> {

    Mark findOneByEvaluator_IdAndEvent_Id(int evaluatorId, int eventId);
}
