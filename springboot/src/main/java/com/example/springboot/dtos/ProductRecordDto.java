package com.example.springboot.dtos;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
/*
    * Os records são imultaveis.
 */

public record ProductRecordDto(@NotBlank String name,@NotNull BigDecimal value) {
}
