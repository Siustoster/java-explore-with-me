package ru.practicum.mainservice.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;
import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "EVENTS", schema = "PUBLIC")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "EVENT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 2000)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @Column(name = "CREATED_ON", nullable = false)
    private LocalDateTime createdOn;
    @Column(nullable = false, length = 7000)
    private String description;
    @Column(name = "EVENT_DATE", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "INITIATOR_ID")
    private User initiator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "LOCATION_ID")
    private Location location;
    @Column(nullable = false)
    @Convert(converter = NumericBooleanConverter.class)
    private Boolean paid;
    @Column(name = "PARTICIPANT_LIMIT", nullable = false)
    private int participantLimit;
    @Column(name = "CONFIRMED_REQUESTS", nullable = false)
    private Integer confirmedRequests;
    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;
    @Column(nullable = false)
    @Convert(converter = NumericBooleanConverter.class)
    private Boolean requestModeration;
    @Enumerated(EnumType.ORDINAL)
    private EventState state;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private int rating;
}
