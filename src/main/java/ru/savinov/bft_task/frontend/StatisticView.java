package ru.savinov.bft_task.frontend;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import lombok.extern.slf4j.Slf4j;
import ru.savinov.bft_task.frontend.dto.CustomerStatisticsDto;
import ru.savinov.bft_task.frontend.dto.StatisticRowDto;
import ru.savinov.bft_task.frontend.factory.GridFactory;
import ru.savinov.bft_task.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route("statistics")
@PageTitle("Статистика клиентов")
public class StatisticView extends VerticalLayout {


    public StatisticView(CustomerService customerService) {
        setSizeFull();
        CustomerStatisticsDto stats = customerService.getStatistics();
        Grid<StatisticRowDto> grid = createStatisticsGrid(stats);
        add(grid);
    }

    private Grid<StatisticRowDto> createStatisticsGrid(CustomerStatisticsDto stats) {
        List<StatisticRowDto> rows = new ArrayList<>();

        rows.add(StatisticRowDto.builder()
                .metric("Количество клиентов")
                .value(String.valueOf(stats.getTotalCustomers()))
                .build());

        rows.add(StatisticRowDto.builder()
                .metric("Средний возраст")
                .value(String.format("%.1f лет", stats.getAvgAge()))
                .build());

        rows.add(StatisticRowDto.builder()
                .metric("Минимальный возраст")
                .value(stats.getMinAge() + " лет")
                .build());

        rows.add(StatisticRowDto.builder()
                .metric("Максимальный возраст")
                .value(stats.getMaxAge() + " лет")
                .build());

        rows.add(StatisticRowDto.builder()
                .metric("Общая сумма")
                .value(String.format("%,.2f руб.", stats.getTotalPayment()))
                .build());

        rows.add(StatisticRowDto.builder()
                .metric("Средний платеж")
                .value(String.format("%,.2f руб.", stats.getAvgPayment()))
                .build());

        rows.add(StatisticRowDto.builder()
                .metric("Минимальный платеж")
                .value(String.format("%,.2f руб.", stats.getMinPayment()))
                .build());

        rows.add(StatisticRowDto.builder()
                .metric("Максимальный платеж")
                .value(String.format("%,.2f руб.", stats.getMaxPayment()))
                .build());

        return GridFactory.builder(StatisticRowDto.class)
                .columns("metric", "value")
                .columnHeader("metric", "Метрика")
                .columnHeader("value", "Значение")
                .withItems(rows)
                .build();
    }
}