package ru.semura.metalambda;

import java.lang.invoke.*;
import java.util.function.Supplier;

public final class MetaLambda {
    private MetaLambda() {
    }

    public static  <T> Supplier<T> createSupplierByConstructor(Class<T> clazz) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle constructor = lookup.findConstructor(clazz, MethodType.methodType(void.class));

        CallSite callSite = LambdaMetafactory.metafactory(
                lookup,
                "get",
                MethodType.methodType(Supplier.class),
                MethodType.methodType(Object.class),
                constructor,
                MethodType.methodType(clazz)
        );

        //noinspection unchecked
        return (Supplier<T>) callSite.getTarget().invoke();
    }

}
