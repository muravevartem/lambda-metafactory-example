# [LambdaMetafactory](https://docs.oracle.com/javase%2F8%2Fdocs%2Fapi%2F%2F/java/lang/invoke/LambdaMetafactory.html)

Создание производителя (*Supplier*) экземляров класса A на основе контсруктора без параметров

```java
    private static Supplier<A> createSupplier(Class<? extends A> aggregateType) throws Throwable {
        CallSite metafactory = LambdaMetafactory.metafactory(
                MethodHandles.lookup(),
                "get",
                MethodType.methodType(Supplier.class),
                MethodType.methodType(Object.class),
                MethodHandles.privateLookupIn(aggregateType, MethodHandles.lookup()).findConstructor(aggregateType, MethodType.methodType(void.class)),
                MethodType.methodType(aggregateType));
        MethodHandle factory = metafactory.getTarget();
        return (Supplier<A>) factory.invoke();
    }
```
