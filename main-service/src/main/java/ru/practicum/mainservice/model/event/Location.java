package ru.practicum.mainservice.model.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LOCATIONS", schema = "PUBLIC")
public class Location {
    @Id
    @Column(name = "LOCATION_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false)
    double lat;
    @Column(nullable = false)
    double lon;
}
