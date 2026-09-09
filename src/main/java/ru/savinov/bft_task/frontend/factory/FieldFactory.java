package ru.savinov.bft_task.frontend.factory;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import static org.springframework.util.StringUtils.hasText;

public class FieldFactory {

    public static TextFieldBuilder textFieldBuilder() {
        return new TextFieldBuilder();
    }

    public static NumberFieldBuilder numberFieldBuilder() {
        return new NumberFieldBuilder();
    }

    public static class TextFieldBuilder {
        private String text = null;
        private String label = null;
        private boolean required = true;
        private String requiredMessage = "Поле обязательно для заполнения";

        public TextFieldBuilder text(String text) {
            this.text = text;
            return this;
        }

        public TextFieldBuilder label(String label) {
            this.label = label;
            return this;
        }

        public TextFieldBuilder required(boolean required) {
            this.required = required;
            return this;
        }

        public TextField build() {
            TextField field = new TextField(hasText(text) ? text : null);

            if (required) {
                field.setRequiredIndicatorVisible(true);
                field.setErrorMessage(requiredMessage);
            }

            if (hasText(label)) {
                field.setLabel(label);
            }

            return field;
        }
    }

    public static class NumberFieldBuilder {
        private String label = null;

        public NumberFieldBuilder label(String label) {
            this.label = label;
            return this;
        }

        public NumberField build() {
            NumberField field = new NumberField();

            if (hasText(label)) {
                field.setLabel(label);
            }

            return field;
        }
    }
}
