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
    protected int id;
    @Column(nullable = false, length = 2000)
    protected String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category;
    @Column(name = "CREATED_ON", nullable = false)
    protected LocalDateTime createdOn;
    @Column(nullable = false, length = 7000)
    protected String description;
    @Column(name = "EVENT_DATE", nullable = false)
    protected LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "INITIATOR_ID")
    protected User initiator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "LOCATION_ID")
    protected Location location;
    @Column(nullable = false)
    @Convert(converter = NumericBooleanConverter.class)
    protected Boolean paid;
    @Column(name = "PARTICIPANT_LIMIT", nullable = false)
    protected int participantLimit;
    @Column(name = "CONFIRMED_REQUESTS", nullable = false)
    protected Integer confirmedRequests;
    @Column(name = "PUBLISHED_ON")
    protected LocalDateTime publishedOn;
    @Column(nullable = false)
    @Convert(converter = NumericBooleanConverter.class)
    protected Boolean requestModeration;
    @Enumerated(EnumType.ORDINAL)
    protected EventState state;
    @Column(nullable = false)
    protected String title;
}
