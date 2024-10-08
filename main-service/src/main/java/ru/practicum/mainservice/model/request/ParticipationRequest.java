package ru.practicum.mainservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.enums.ParticipationRequestStatus;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "PARTICIPATION_REQUESTS", schema = "PUBLIC")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ParticipationRequest {
    @Id
    @Column(name = "REQUEST_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "EVENT_ID")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "REQUESTER_ID")
    private User requester;
    @Enumerated(EnumType.ORDINAL)
    private ParticipationRequestStatus status;
}
