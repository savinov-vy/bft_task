package ru.savinov.bft_task.frontend.factory;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class GridFactory {

    public static final String HEIGHT_DEFAULT = "300px";
    public static final int ID_COLUMN_WIDTH = 50;

    public static <T> GridBuilder<T> builder(Class<T> beanType) {
        return new GridBuilder<T>()
                .withBean(beanType);
    }

    public static <T> Grid<T> create(Class<T> beanType) {
        return builder(beanType).build();
    }

    public static class GridBuilder<T> {
        private Class<T> beanType;
        private String height = HEIGHT_DEFAULT;
        private List<String> columns;
        private boolean withIdColumn = false;
        private Collection<T> items;
        private List<GridVariant> variants;
        private boolean withSelection = true;
        private boolean isVisible = true;
        private Consumer<T> selectionListener;

        private GridBuilder() {
        }

        public GridBuilder<T> withBean(Class<T> beanType) {
            this.beanType = beanType;
            return this;
        }

        public GridBuilder<T> height(String height) {
            this.height = height;
            return this;
        }



        public GridBuilder<T> columns(String... columns) {
            this.columns = Arrays.asList(columns);
            return this;
        }

        public GridBuilder<T> withIdColumn() {
            this.withIdColumn = true;
            return this;
        }

        public GridBuilder<T> withItems(Collection<T> items) {
            this.items = items;
            return this;
        }

        public GridBuilder<T> withVariants(GridVariant... variants) {
            this.variants = Arrays.asList(variants);
            return this;
        }

        public GridBuilder<T> withSelection(boolean withSelection) {
            this.withSelection = withSelection;
            return this;
        }

        public GridBuilder<T> visible(boolean visible) {
            this.isVisible = visible;
            return this;
        }

        public GridBuilder<T> onSelect(Consumer<T> selectionListener) {
            this.selectionListener = selectionListener;
            return this;
        }

        public Grid<T> build() {
            if (beanType == null) {
                throw new IllegalStateException("Bean type must be set using withBean()");
            }

            Grid<T> grid = new Grid<>(beanType);
            grid.setHeight(height);
            grid.setVisible(isVisible);

            if (columns != null && !columns.isEmpty()) {
                grid.setColumns(columns.toArray(new String[0]));
            }

            if (withIdColumn && columns != null && columns.contains("id")) {
                grid.getColumnByKey("id")
                        .setWidth(ID_COLUMN_WIDTH + "px")
                        .setFlexGrow(0);
            }

            if (variants != null && !variants.isEmpty()) {
                for (GridVariant variant : variants) {
                    grid.addThemeVariants(variant);
                }
            }

            if (items != null) {
                grid.setItems(items);
            }

            if (withSelection) {
                grid.asSingleSelect();
            }

            if (withSelection) {
                grid.asSingleSelect().addValueChangeListener(e -> {
                    if (selectionListener != null) {
                        selectionListener.accept(e.getValue());
                    }
                });
            }

            return grid;
        }
    }
}
