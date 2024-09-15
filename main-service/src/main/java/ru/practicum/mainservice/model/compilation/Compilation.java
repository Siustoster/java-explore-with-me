package ru.practicum.mainservice.model.compilation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;
import ru.practicum.mainservice.model.event.Event;

import java.util.Set;

@Entity
@Table(name = "COMPILATIONS", schema = "PUBLIC")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @Column(name = "COMPILATION_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(nullable = false)
    protected String title;
    @Column(nullable = false)
    @Convert(converter = NumericBooleanConverter.class)
    protected Boolean pinned;
    @ManyToMany
    @JoinTable(name = "EVENT_COMPILATIONS",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
    protected Set<Event> events;
}