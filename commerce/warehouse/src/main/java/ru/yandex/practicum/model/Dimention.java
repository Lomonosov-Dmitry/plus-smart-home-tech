package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class Dimention {

    @Column(name = "width")
    Double width;

    @Column(name = "height")
    Double height;

    @Column(name = "depth")
    Double depth;
}
