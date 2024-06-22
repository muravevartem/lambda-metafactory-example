package ru.semura.system;

import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws Throwable {
        Supplier<A> supplier = createSupplier(A.class);
        Supplier<A> supplierA = A::new;

        long sum;
        for (int j = 0 ; j < 100000; j++) {
            sum = 0;
            for (int i = 0; i < 100; i++) {
                long time = test0();
                sum += time;
            }

            sum = 0;
            for (int i = 0; i < 100; i++) {
                long time = test1(supplier);
                sum += time;
            }

            sum = 0;
            for (int i = 0; i < 100; i++) {
                long time = test2();
                sum += time;
            }

            sum = 0;
            for (int i = 0; i < 100; i++) {
                long time = test1(() -> new A());
                sum += time;
            }

            sum = 0;
            for (int i = 0; i < 100; i++) {
                long time = test1(supplierA);
                sum += time;
            }
        }

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = test0();
            sum += time;
        }
        System.out.println("Default\t" + sum/100. + "ns");

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = test1(supplier);
            sum += time;
        }
        System.out.println("MetaLambda\t" + sum/100. + "ns");

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = test2();
            sum += time;
        }
        System.out.println("Reflection\t" + sum/100. + "ns");

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = test1(() -> new A());
            sum += time;
        }
        System.out.println("Stupid Supplier\t" + sum/100. + "ns");

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = test1(supplierA);
            sum += time;
        }
        System.out.println("Simple Supplier\t" + sum/100. + "ns");

    }

    private static long test0() {
        long start1 = System.nanoTime();
        A a = new A();
        if (false) {
            a.toString();
        }
        long stop1 = System.nanoTime() - start1;
        return stop1;
    }

    private static long test1(Supplier<A> supplier) {
        long start1 = System.nanoTime();
        A a = supplier.get();
        long stop1 = System.nanoTime() - start1;
        return stop1;
    }

    private static long test2() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        long start2 = System.nanoTime();
        A a1 = A.class.getDeclaredConstructor().newInstance();
        long stop2 = System.nanoTime() - start2;
        return stop2;
    }

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

    public static class A {
        @Override
        public String toString() {
            return "A{}";
        }

        private A() {
        }
    }

}