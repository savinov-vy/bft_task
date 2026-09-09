package ru.savinov.bft_task.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CustomerStatisticsDto {
    private Double avgAge;
    private Integer minAge;
    private Integer maxAge;
    private BigDecimal avgPayment;
    private BigDecimal minPayment;
    private BigDecimal maxPayment;
    private BigDecimal totalPayment;
    private Long totalCustomers;
}
