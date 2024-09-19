package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.rate.Rate;

public interface RateRepository extends JpaRepository<Rate, Integer> {

    Rate findOneByEvaluator_IdAndEvent_Id(int evaluatorId, int eventId);
}
