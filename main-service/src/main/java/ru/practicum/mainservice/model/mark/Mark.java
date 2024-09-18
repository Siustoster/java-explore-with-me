package ru.practicum.mainservice.model.mark;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.user.User;

@Entity
@Table(name = "MARKS", schema = "PUBLIC")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mark {
    @Id
    @Column(name = "MARK_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "EVALUATOR_ID")
    protected User evaluator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "EVENT_ID")
    protected Event event;
    @Column(nullable = false)
    @Convert(converter = NumericBooleanConverter.class)
    protected Boolean score;
}
