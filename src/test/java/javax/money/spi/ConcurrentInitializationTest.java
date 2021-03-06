/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package javax.money.spi;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.money.DummyAmount;
import javax.money.DummyAmountBuilder;
import javax.money.MonetaryAmount;
import javax.money.Monetary;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Test concurrent initialization/bootstrapping of {@link Bootstrap}.
 */
public class ConcurrentInitializationTest {

    static class Round implements Runnable {
        private final MonetaryAmount amount;

        Round(final MonetaryAmount amount) {
            this.amount = amount;
        }

        @Override
        public void run() {
            Monetary.getDefaultRounding().apply(amount);
        }
    }

    private static final int THREAD_COUNT = 100;

    /**
     * https://github.com/JavaMoney/jsr354-ri/issues/30.
     */
    @Test
    public void shouldSupportConcurrentInitialization() throws ExecutionException, InterruptedException {
        final DummyAmount amount = new DummyAmountBuilder().create();

        final List<Thread> threads = new ArrayList<>(THREAD_COUNT);
        final List<Throwable> throwables = Collections.synchronizedList(new ArrayList<Throwable>());
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                throwables.add(e);
            }
        };

        for (int i = 0; i < THREAD_COUNT; i++) {
            final Thread thread = new Thread(new Round(amount));
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
        for(Throwable th:throwables){
            th.printStackTrace();
        }
        assertEquals(0, throwables.size());

    }
}
