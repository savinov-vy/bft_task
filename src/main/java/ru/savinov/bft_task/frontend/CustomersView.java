package ru.savinov.bft_task.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import ru.savinov.bft_task.frontend.dto.CustomerDto;
import ru.savinov.bft_task.frontend.factory.ButtonFactory;
import ru.savinov.bft_task.frontend.factory.FieldFactory;
import ru.savinov.bft_task.frontend.factory.GridFactory;
import ru.savinov.bft_task.frontend.factory.HorizontalLayoutFactory;
import ru.savinov.bft_task.service.CustomerService;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Route("")
public class CustomersView extends VerticalLayout {
    private final CustomerService customerService;

    private final CustomerEditor editor;

    private Grid<CustomerDto> grid;

    private Button searchBtn;
    private Button addNewBtn;
    private Button statisticBtn;
    private HorizontalLayout actions;

    private HorizontalLayout filterLayout;
    private TextField lastNameFilter;
    private TextField ageFilter;


    public CustomersView(CustomerService customerService, CustomerEditor editor) {
        this.customerService = customerService;
        this.editor = editor;
        initUiComponents();
        add(actions, grid, editor);
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(lastNameFilter.getValue(), ageFilter.getValue());
        });
        listCustomers();
    }

    private void initUiComponents() {
        grid = GridFactory.builder(CustomerDto.class)
                .columns("id", "firstName", "lastName", "age", "payment")
                .columnHeader("id", "id")
                .columnHeader("firstName", "Имя")
                .columnHeader("lastName", "Фамилия")
                .columnHeader("age", "Возраст")
                .columnHeader("payment", "Платеж")
                .onSelect(editor::editCustomer)
                .build();
        lastNameFilter = FieldFactory.textFieldBuilder()
                .required(false)
                .text("Фамилия")
                .build();
        ageFilter = FieldFactory.textFieldBuilder()
                .required(false)
                .text("Возраст")
                .build();
        searchBtn = ButtonFactory.builder()
                .text("Поиск")
                .icon(VaadinIcon.SEARCH)
                .withClickListener(e -> listCustomers(lastNameFilter.getValue(), ageFilter.getValue()))
                .build();
        filterLayout = HorizontalLayoutFactory.builder()
                .add(lastNameFilter, ageFilter, searchBtn)
                .build();
        addNewBtn = ButtonFactory.builder()
                .text("Добавить")
                .with("150px")
                .icon(VaadinIcon.PLUS)
                .withClickListener(e -> editor.editCustomer(CustomerDto.builder()
                        .build()))
                .build();
        statisticBtn = ButtonFactory.builder()
                .text("Статистика")
                .with("150px")
                .icon(VaadinIcon.CHART)
                .withClickListener(e -> {
                    log.info("Открытие статистики в новой вкладке");
                    UI.getCurrent().getPage().open("/statistics", "_blank");
                })
                .build();
        actions = HorizontalLayoutFactory.builder()
                .add(filterLayout, addNewBtn, statisticBtn)
                .build();
    }

    private void listCustomers(String lastNameFilter, String ageFilter) {
        if (hasText(lastNameFilter) || hasText(ageFilter)) {
            grid.setItems(customerService.findByFilter(lastNameFilter, castFilter(ageFilter)));
        } else {
            grid.setItems(customerService.findAll());
        }
    }

    private void listCustomers() {
        grid.setItems(customerService.findAll());
    }

    private Integer castFilter(String number) {
        if (!hasText(number)) {
            return null;
        }
        try {
            return Integer.parseInt(number.trim());
        } catch (NumberFormatException e) {
            log.warn("Фильтр: {} в классе: {} не удалось преобразовать в Integer", number, getClass().getName());
            return null;
        }

    }

}
