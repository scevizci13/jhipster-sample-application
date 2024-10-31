package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SatisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Satis getSatisSample1() {
        return new Satis().id(1L).miktar(1).odemeDurumu("odemeDurumu1").teslimatAdresi("teslimatAdresi1").siparisNotu("siparisNotu1");
    }

    public static Satis getSatisSample2() {
        return new Satis().id(2L).miktar(2).odemeDurumu("odemeDurumu2").teslimatAdresi("teslimatAdresi2").siparisNotu("siparisNotu2");
    }

    public static Satis getSatisRandomSampleGenerator() {
        return new Satis()
            .id(longCount.incrementAndGet())
            .miktar(intCount.incrementAndGet())
            .odemeDurumu(UUID.randomUUID().toString())
            .teslimatAdresi(UUID.randomUUID().toString())
            .siparisNotu(UUID.randomUUID().toString());
    }
}
