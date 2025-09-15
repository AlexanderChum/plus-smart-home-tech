package ru.practicum.warehouse.Models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    @NotNull
    @Min(value = 1)
    Double width;

    @NotNull
    @Min(value = 1)
    Double height;

    @NotNull
    @Min(value = 1)
    Double depth;
}
