package ru.savinov.bft_task.frontend;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import ru.savinov.bft_task.frontend.dto.CustomerDto;
import ru.savinov.bft_task.service.CustomerService;

@Route
public class CustomersView extends VerticalLayout {
    private final CustomerService customerService;

    private final CustomerEditor editor;

    final Grid<CustomerDto> grid;

    final TextField filter;

    private final Button addNewBtn;

    public CustomersView(CustomerService customerService, CustomerEditor editor) {
        this.customerService = customerService;
        this.editor = editor;
        this.grid = new Grid<>(CustomerDto.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter by last name");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(
                e -> editor.editCustomer(CustomerDto.builder()
                                                                .lastName("")
                                                                .firstName("")
                                                                .build())
        );

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        // Initialize listing
        listCustomers(null);
    }

    // tag::listCustomers[]
    void listCustomers(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(customerService.findByLastName(filterText));
        } else {
            grid.setItems(customerService.findAll());
        }
    }
    // end::listCustomers[]

}
