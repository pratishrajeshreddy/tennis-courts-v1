package com.tenniscourts.guests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateGuestRequestDTO {
    @NotNull
    private String name;
}
