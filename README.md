# LambdaMetafactory

## Создание

```java
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
```

## Benchmark

| Метод       | Результат              | Сравнение с нативным способом |
|-------------|------------------------|-------------------------------|
| MetaLambda  | 1,585 ±  0,025  ops/ns | -39.9%                        |
| Reflection  | 0,031 ±  0,001  ops/ns | -99.9%                        |
| Конструктор | 2,640 ±  0,060  ops/ns | -0%                           |

