package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.enums.QuantityState;

@Getter
@Setter
public class SetProductQuantityStateRequest {
    @NotNull
    Long productId;

    @NotNull
    QuantityState state;
}
