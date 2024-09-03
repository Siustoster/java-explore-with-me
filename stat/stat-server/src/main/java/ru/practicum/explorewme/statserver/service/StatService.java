package ru.practicum.explorewme.statserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.StatDto;
import ru.practicum.explorewme.statserver.mapper.HitMapper;
import ru.practicum.explorewme.statserver.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository repository;
    private final HitMapper mapper;

    @Transactional
    public void createHit(HitRequestDto hitRequestDto) {
        repository.save(mapper.dtoToHit(hitRequestDto));
    }

    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, Boolean uniq, List<String> uri) {
        if (uniq)
            if (uri.isEmpty())
                return repository.getUniqueStatWOUri(start, end);
            else
                return repository.getUniqueStatWUri(uri, start, end);
        else if (uri.isEmpty())
            return repository.getHitsStatWOUri(start, end);
        else
            return repository.getHitsStatWUri(uri, start, end);
    }
}
