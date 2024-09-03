package ru.practicum.explorewme.statserver.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewme.HitRequestDto;
import ru.practicum.explorewme.statserver.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    @Mapping(source = "timestamp", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Hit dtoToHit(HitRequestDto dto);
}
