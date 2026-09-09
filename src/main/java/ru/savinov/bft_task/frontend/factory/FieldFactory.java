package ru.savinov.bft_task.frontend.factory;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.function.Consumer;

import static org.springframework.util.StringUtils.hasText;

public class FieldFactory {

    public static final String TEXT_DEFAULT = "";

    public static FieldBuilder builder() {
        return new FieldBuilder();
    }

    public static class FieldBuilder {
        private Consumer<String> valueChangeListener;
        private String text;
        private boolean lazyMode = false;

        public FieldBuilder text(String text) {
            this.text = text;
            return this;
        }

        public FieldBuilder onChange(Consumer<String> listener) {
            this.valueChangeListener = listener;
            return this;
        }

        public FieldBuilder lazy(boolean lazy) {
            this.lazyMode = lazy;
            return this;
        }

        public TextField build() {
            TextField field = new TextField(hasText(text) ? text : TEXT_DEFAULT);

            if (lazyMode) {
                field.setValueChangeMode(ValueChangeMode.LAZY);
            }

            if (valueChangeListener != null) {
                field.addValueChangeListener(e -> {
                    String value = e.getValue();
                    valueChangeListener.accept(value != null ? value : "");
                });
            }

            return field;
        }
    }
}
