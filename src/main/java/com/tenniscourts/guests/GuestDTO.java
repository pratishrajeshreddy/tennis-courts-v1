package com.tenniscourts.guests;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GuestDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Valid
    @Size(max=255)
    @ApiModelProperty(required = true)
    private String name;

    public void setName(String name) {
        this.name = name.trim();
    }
}
