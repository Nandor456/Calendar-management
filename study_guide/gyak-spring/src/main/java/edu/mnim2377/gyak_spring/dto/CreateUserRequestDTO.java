package edu.mnim2377.gyak_spring.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequestDTO {
    @NotBlank(message = "A név nem lehet üres")
    @Size(min = 2, max = 50, message = "A név hossza 2-50 karakter között kell legyen")
    private String name;

    @NotNull(message = "Az életkor nem lehet null")
    @Min(value = 18, message = "Az életkornak minimum 18-nak kell lennie")
    @Max(value = 120, message = "Az életkor nem lehet több mint 120")
    private int age;
}
