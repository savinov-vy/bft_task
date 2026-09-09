package ru.savinov.bft_task.frontend.factory;

import com.vaadin.flow.component.grid.Grid;

import java.util.*;
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
        private boolean withSelection = true;
        private boolean isVisible = true;
        private Consumer<T> selectionListener;
        private Map<String, String> columnHeaders = new LinkedHashMap<>();
        private Collection<T> items;

        private GridBuilder() {
        }

        public GridBuilder<T> withBean(Class<T> beanType) {
            this.beanType = beanType;
            return this;
        }

        public GridBuilder<T> columns(String... columns) {
            this.columns = Arrays.asList(columns);
            return this;
        }

        public GridBuilder<T> columnHeader(String columnKey, String header) {
            this.columnHeaders.put(columnKey, header);
            return this;
        }

        public GridBuilder<T> withItems(Collection<T> items) {
            this.items = items;
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

            if (withSelection) {
                grid.asSingleSelect();
            }

            for (Map.Entry<String, String> entry : columnHeaders.entrySet()) {
                Grid.Column<T> column = grid.getColumnByKey(entry.getKey());
                if (column != null) {
                    column.setHeader(entry.getValue());
                }
            }

            if (withSelection) {
                grid.asSingleSelect().addValueChangeListener(e -> {
                    if (selectionListener != null) {
                        selectionListener.accept(e.getValue());
                    }
                });
            }

            if (items != null && !items.isEmpty()) {
                grid.setItems(items);
            }

            return grid;
        }
    }
}
