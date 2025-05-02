package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "scenarios")
public class Scenario {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    long id;

    @Column(name = "hub_id")
    String hubId;

    @Column(name = "name")
    String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "scenario_conditions",
        joinColumns = {@JoinColumn(name = "scenario_id", referencedColumnName = "id")},
    inverseJoinColumns = @JoinColumn(name = "condition_id"))
    @MapKeyColumn(table = "scenario_conditions", name = "sensor_id")
    Map<String, Condition> conditions = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "scenario_actions",
            joinColumns = {@JoinColumn(name = "scenario_id", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    @MapKeyColumn(table = "scenario_actions", name = "sensor_id")
    Map<String, Action> actions = new HashMap<>();
}
