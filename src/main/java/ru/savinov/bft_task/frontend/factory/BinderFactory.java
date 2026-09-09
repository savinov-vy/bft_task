package ru.savinov.bft_task.frontend.factory;


import com.vaadin.flow.data.binder.Binder;


public class BinderFactory {

    public static <T> BinderBuilder<T> builder(Class<T> beanType) {
        return new BinderBuilder<>(beanType);
    }

    public static class BinderBuilder<T> {
        private final Class<T> beanType;

        private BinderBuilder(Class<T> beanType) {
            this.beanType = beanType;
        }

        public Binder<T> build() {
            return new Binder<>(beanType);
        }
    }
}
