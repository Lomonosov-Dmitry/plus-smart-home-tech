package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "conditions")
public class Condition {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    ConditionTypeAvro type;

    @Enumerated(EnumType.STRING)
    ConditionOperationAvro operation;

    @Column(name = "value")
    Integer value;
}
