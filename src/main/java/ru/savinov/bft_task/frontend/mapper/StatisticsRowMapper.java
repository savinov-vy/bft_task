package ru.savinov.bft_task.frontend.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.savinov.bft_task.frontend.dto.CustomerStatisticsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StatisticsRowMapper implements RowMapper<CustomerStatisticsDto> {
    @Override
    public CustomerStatisticsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return CustomerStatisticsDto.builder()
                .avgAge(rs.getDouble("avg_age"))
                .minAge(rs.getInt("min_age"))
                .maxAge(rs.getInt("max_age"))
                .avgPayment(rs.getBigDecimal("avg_payment"))
                .minPayment(rs.getBigDecimal("min_payment"))
                .maxPayment(rs.getBigDecimal("max_payment"))
                .totalCustomers(rs.getLong("total_customers"))
                .totalPayment(rs.getBigDecimal("total_payment"))
                .build();
    }
}
