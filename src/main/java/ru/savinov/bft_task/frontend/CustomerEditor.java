package ru.savinov.bft_task.frontend;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import ru.savinov.bft_task.entity.Customer;
import ru.savinov.bft_task.factory.ButtonFactory;
import ru.savinov.bft_task.factory.FieldFactory;
import ru.savinov.bft_task.factory.HorizontalLayoutFactory;
import ru.savinov.bft_task.repository.CustomerRepository;


@SpringComponent
@UIScope
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerEditor extends VerticalLayout implements KeyNotifier {

    final CustomerRepository repository;
    Customer customer;

    TextField firstName;
    TextField lastName;

    Button save;
    Button cancel;
    Button delete;
    HorizontalLayout actions;

    Binder<Customer> binder = new Binder<>(Customer.class);
    private ChangeHandler changeHandler;

    @Autowired
    public CustomerEditor(CustomerRepository repository) {
        initFields();
        this.repository = repository;

        add(firstName, lastName, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        addKeyPressListener(Key.ENTER, e -> save());

        setVisible(false);
    }

    private void initFields() {
        lastName = FieldFactory.builder()
                .text("Last Name")
                .build();
        firstName = FieldFactory.builder()
                .text("First Name")
                .build();
        save = ButtonFactory.builder()
                .icon(VaadinIcon.CHECK)
                .text("Save")
                .theme(ButtonVariant.LUMO_PRIMARY)
                .withClickListener(e -> save())
                .build();
        cancel = ButtonFactory.builder()
                .text("Cancel")
                .withClickListener(e -> editCustomer(customer))
                .build();
        delete = ButtonFactory.builder()
                .theme(ButtonVariant.LUMO_ERROR)
                .text("Delete")
                .icon(VaadinIcon.TRASH)
                .withClickListener(e -> delete())
                .build();
        actions = HorizontalLayoutFactory.builder()
                .add(save, cancel, delete)
                .build();
    }

    void delete() {
        repository.delete(customer);
        changeHandler.onChange();
    }

    void save() {
        repository.save(customer);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCustomer(Customer c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            // In a more complex app, you might want to load
            // the entity/DTO with lazy loaded relations for editing
            customer = repository.findById(c.getId()).get();
        } else {
            customer = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        setVisible(true);

        // Focus first name initially
        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

}
