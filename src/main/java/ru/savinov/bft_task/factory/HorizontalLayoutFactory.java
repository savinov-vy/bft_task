package ru.savinov.bft_task.factory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HorizontalLayoutFactory {

    public static HorizontalLayoutBuilder builder() {
        return new HorizontalLayoutBuilder();
    }

    public static class HorizontalLayoutBuilder {
        private final List<Component> components = new ArrayList<>();

        public HorizontalLayoutBuilder add(Component... components) {
            this.components.addAll(Arrays.asList(components));
            return this;
        }

        public HorizontalLayoutBuilder add(Component component) {
            this.components.add(component);
            return this;
        }

        public HorizontalLayout build() {
            HorizontalLayout layout = new HorizontalLayout();
            layout.add(components.toArray(new Component[0]));
            return layout;
        }
    }
}