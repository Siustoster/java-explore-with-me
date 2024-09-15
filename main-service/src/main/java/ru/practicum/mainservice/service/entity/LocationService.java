package ru.practicum.mainservice.service.entity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.model.event.Location;
import ru.practicum.mainservice.repository.LocationRepository;

@Slf4j
@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    /**
     * Вызывается методами EventService, к которым уже применяется Transactional
     */
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}