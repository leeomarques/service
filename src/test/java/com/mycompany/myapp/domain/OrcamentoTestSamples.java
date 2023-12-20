package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrcamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Orcamento getOrcamentoSample1() {
        return new Orcamento().id(1L).ano(1).mes("mes1");
    }

    public static Orcamento getOrcamentoSample2() {
        return new Orcamento().id(2L).ano(2).mes("mes2");
    }

    public static Orcamento getOrcamentoRandomSampleGenerator() {
        return new Orcamento().id(longCount.incrementAndGet()).ano(intCount.incrementAndGet()).mes(UUID.randomUUID().toString());
    }
}
