package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    ActionTypeAvro type;

    @Column(name = "value")
    Integer value;
}
