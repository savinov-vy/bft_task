package ru.savinov.bft_task.frontend.factory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.Consumer;

import static org.springframework.util.StringUtils.hasText;

public class ButtonFactory {

    public static final String WITH_DEFAULT = "100px";
    public static final String TEXT_DEFAULT = "";

    public static ButtonBuilder cancelBtn() {
        return ButtonFactory.builder()
                .text("Cancel");
    }

    public static ButtonBuilder deleteBtn() {
        return ButtonFactory.builder()
                .theme(ButtonVariant.LUMO_ERROR)
                .text("Delete")
                .icon(VaadinIcon.TRASH);
    }

    public static ButtonBuilder saveBtn() {
        return ButtonFactory.builder()
                .icon(VaadinIcon.CHECK)
                .text("Save")
                .theme(ButtonVariant.LUMO_PRIMARY);
    }

    public static ButtonBuilder builder() {
        return new ButtonBuilder();
    }

    public static class ButtonBuilder {
        private String text;
        private String with = WITH_DEFAULT;
        private VaadinIcon icon;
        private ButtonVariant[] variants = {};
        private Consumer<Button> clickListener;

        public ButtonBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ButtonBuilder icon(VaadinIcon icon) {
            this.icon = icon;
            return this;
        }

        public ButtonBuilder with(String with) {
            this.with = with;
            return this;
        }

        public ButtonBuilder theme(ButtonVariant buttonVariant) {
            this.variants = new ButtonVariant[]{buttonVariant};
            return this;
        }

        public ButtonBuilder withClickListener(Consumer<Button> clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public Button build() {
            text = hasText(text) ? text : TEXT_DEFAULT;
            Button button = icon != null
                    ? new Button(text, icon.create())
                    : new Button(text);

            if (clickListener != null) {
                button.addClickListener(e -> clickListener.accept(button));
            }

            button.setWidth(with);
            button.setEnabled(true);

            if (variants.length > 0) {
                button.addThemeVariants(variants);
            }
            return button;
        }
    }
}
