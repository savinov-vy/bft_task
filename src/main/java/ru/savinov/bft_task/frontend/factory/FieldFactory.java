package ru.savinov.bft_task.frontend.factory;

import com.vaadin.flow.component.textfield.TextField;

import static org.springframework.util.StringUtils.hasText;

public class FieldFactory {

    public static FieldBuilder builder() {
        return new FieldBuilder();
    }

    public static class FieldBuilder {
        private String text = null;
        private String label = null;
        private boolean required = false;
        private String requiredMessage = "Поле обязательно для заполнения";

        public FieldBuilder text(String text) {
            this.text = text;
            return this;
        }

        public FieldBuilder label(String label) {
            this.label = label;
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
}
