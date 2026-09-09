package ru.savinov.bft_task.frontend;

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

    private Button addNewBtn;
    private Button searchBtn;
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
                .onSelect(editor::editCustomer)
                .build();
        lastNameFilter = FieldFactory.builder()
                .text("Фамилия")
                .build();
        ageFilter = FieldFactory.builder()
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
                .icon(VaadinIcon.PLUS)
                .withClickListener(e -> editor.editCustomer(CustomerDto.builder()
                        .build()))
                .build();
        actions = HorizontalLayoutFactory.builder()
                .add(filterLayout, addNewBtn)
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
