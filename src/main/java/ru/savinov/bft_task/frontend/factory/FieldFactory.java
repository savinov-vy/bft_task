package ru.savinov.bft_task.frontend.factory;

import com.vaadin.flow.component.textfield.TextField;

import static org.springframework.util.StringUtils.hasText;

public class FieldFactory {

    public static final String TEXT_DEFAULT = "";

    public static FieldBuilder builder() {
        return new FieldBuilder();
    }

    public static class FieldBuilder {
        private String text;

        public FieldBuilder text(String text) {
            this.text = text;
            return this;
        }

        public TextField build() {
            return new TextField(hasText(text) ? text : TEXT_DEFAULT);
        }
    }
}
