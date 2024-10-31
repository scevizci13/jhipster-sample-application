package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UrunTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Urun getUrunSample1() {
        return new Urun().id(1L).isim("isim1").lisansAnahtari("lisansAnahtari1").aciklama("aciklama1");
    }

    public static Urun getUrunSample2() {
        return new Urun().id(2L).isim("isim2").lisansAnahtari("lisansAnahtari2").aciklama("aciklama2");
    }

    public static Urun getUrunRandomSampleGenerator() {
        return new Urun()
            .id(longCount.incrementAndGet())
            .isim(UUID.randomUUID().toString())
            .lisansAnahtari(UUID.randomUUID().toString())
            .aciklama(UUID.randomUUID().toString());
    }
}
