package ru.semura.metalambda;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
public class MetaLambdaBenchmark {

    private Supplier<Dummy> supplierByMetaLambda;

    @Setup
    public void setup() throws Throwable {
        this.supplierByMetaLambda = MetaLambda.createSupplierByConstructor(Dummy.class);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void benchMetaLambda() {
        Dummy dummy = supplierByMetaLambda.get();
        Objects.requireNonNull(dummy);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void benchReflection() throws Throwable {
        Constructor<Dummy> constructor = Dummy.class.getDeclaredConstructor();
        Dummy dummy = constructor.newInstance();
        Objects.requireNonNull(dummy);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void benchSimple() {
        Dummy dummy = new Dummy();
        Objects.requireNonNull(dummy);
    }

    static class Dummy {
    }

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }
}
