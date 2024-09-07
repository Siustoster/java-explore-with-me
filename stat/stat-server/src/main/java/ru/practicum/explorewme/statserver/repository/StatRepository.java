package ru.practicum.explorewme.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewme.StatDto;
import ru.practicum.explorewme.statserver.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.explorewme.StatDto(h.app, h.uri, count(h.ip)) " +
            "from Hit as h " +
            "where " +
            "(h.uri in (:uri) or (:uri) is null)" +
            "and (h.created between :start and :end) " +
            "group by " +
            "h.app, " +
            "h.uri " +
            "order by count(h.ip) desc, h.uri asc")
    List<StatDto> getHitsStatWUri(
            @Param("uri") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("select new ru.practicum.explorewme.StatDto(h.app, h.uri, count(h.ip)) " +
            "from Hit as h " +
            "where " +
            " (h.created between :start and :end) " +
            "group by " +
            "h.app, " +
            "h.uri " +
            "order by count(h.ip) desc, h.uri asc")
    List<StatDto> getHitsStatWOUri(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("select new ru.practicum.explorewme.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where " +
            "(h.uri in (:uris) or (:uris) is null) " +
            "and (h.created between :start and :end) " +
            "group by " +
            "h.app, " +
            "h.uri " +
            "order by count(distinct h.ip) desc, h.uri asc")
    List<StatDto> getUniqueStatWUri(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("select new ru.practicum.explorewme.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where " +
            "(h.created between :start and :end) " +
            "group by " +
            "h.app, " +
            "h.uri " +
            "order by count(distinct h.ip) desc, h.uri asc")
    List<StatDto> getUniqueStatWOUri(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
