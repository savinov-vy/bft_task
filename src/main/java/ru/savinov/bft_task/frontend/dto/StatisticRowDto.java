package ru.savinov.bft_task.frontend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticRowDto {
    private String metric;
    private String value;
}