package ru.savinov.bft_task.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String lastName;
    private String firstName;
    private Integer age;
    private BigDecimal payment;
}