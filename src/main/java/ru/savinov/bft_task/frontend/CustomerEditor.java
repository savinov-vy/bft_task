package ru.savinov.bft_task.frontend;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.savinov.bft_task.frontend.dto.CustomerDto;
import ru.savinov.bft_task.frontend.factory.BinderFactory;
import ru.savinov.bft_task.frontend.factory.ButtonFactory;
import ru.savinov.bft_task.frontend.factory.FieldFactory;
import ru.savinov.bft_task.frontend.factory.HorizontalLayoutFactory;
import ru.savinov.bft_task.frontend.function.ChangeHandler;
import ru.savinov.bft_task.service.CustomerService;

import java.util.Optional;


@Slf4j
@SpringComponent
@UIScope
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerEditor extends VerticalLayout implements KeyNotifier {

    final CustomerService customerService;
    CustomerDto customer;

    TextField firstName;
    TextField lastName;
    TextField age;
    TextField payment;

    Button saveBtn;
    Button cancelBtn;
    Button deleteBtn;
    HorizontalLayout fields;
    HorizontalLayout actions;
    Binder<CustomerDto> binder;
    private ChangeHandler changeHandler;

    @Autowired
    public CustomerEditor(CustomerService customerService) {
        this.customerService = customerService;

        initUiComponents();

        add(firstName, lastName, age, payment, actions);

        addKeyPressListener(Key.ENTER, e -> save());

        setVisible(false);
    }

    private void initUiComponents() {
        firstName = FieldFactory.builder()
                .label("Имя")
                .build();
        lastName = FieldFactory.builder()
                .label("Фамилия")
                .build();
        age = FieldFactory.builder()
                .label("Возраст")
                .build();
        payment = FieldFactory.builder()
                .label("Платеж")
                .build();
        saveBtn = ButtonFactory.saveBtn()
                .withClickListener(e -> save())
                .build();
        cancelBtn = ButtonFactory.cancelBtn()
                .withClickListener(e -> editCustomer(customer))
                .build();
        deleteBtn = ButtonFactory.deleteBtn()
                .withClickListener(e -> delete())
                .build();
        actions = HorizontalLayoutFactory.builder()
                .add(saveBtn, cancelBtn, deleteBtn)
                .build();
        binder = BinderFactory.builder(CustomerDto.class)
                .build();
        initValidateField();
    }

    private void initValidateField() {
        binder.forField(firstName)
                .asRequired("Имя обязательно")
                .withValidator(value -> value != null && value.length() >= 2,
                        "Имя должно содержать минимум 2 символа")
                .withValidator(value -> value != null && value.length() <= 50,
                        "Имя не может быть длиннее 50 символов")
                .bind(CustomerDto::getFirstName, CustomerDto::setFirstName);
        binder.forField(lastName)
                .asRequired("Фамилия обязательна")
                .withValidator(value -> value != null && value.length() >= 2,
                        "Фамилия должна содержать минимум 2 символа")
                .withValidator(value -> value != null && value.length() <= 50,
                        "Фамилия не может быть длиннее 50 символов")
                .bind(CustomerDto::getLastName, CustomerDto::setLastName);
    }

    private void delete() {
        log.info("Процесс удаления [customer] c параметрами: {} в классе: {}", customer, getClass().getName());
        customerService.delete(customer);
        changeHandler.onChange();
    }

    private void save() {
        if (!binder.validate().isOk()) {
            Notification.show("Проверьте правильность заполнения полей",
                    3000, Notification.Position.MIDDLE);
            return;
        }
        log.info("Процесс сохранения [customer] c параметрами: {} в классе: {}", customer, getClass().getName());
        customerService.save(customer);
        changeHandler.onChange();
    }

    public final void editCustomer(CustomerDto customer) {
        log.info("Процесс редактирования [customer] c параметрами: {} в классе: {}", customer, getClass().getName());
        if (customer == null) {
            setVisible(false);
            return;
        }
        if (isSaved(customer)) {
            this.customer = customerService.findById(customer.getId());
        } else {
            this.customer = customer;
        }
        cancelBtn.setVisible(isSaved(customer));
        binder.setBean(this.customer);
        setVisible(true);
        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }

    private boolean isSaved(CustomerDto customer) {
        return Optional.ofNullable(customer)
                .map(CustomerDto::getId)
                .isPresent();
    }

}
