package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MusteriTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Musteri getMusteriSample1() {
        return new Musteri().id(1L).ad("ad1").soyad("soyad1").email("email1").telefon("telefon1").adres("adres1");
    }

    public static Musteri getMusteriSample2() {
        return new Musteri().id(2L).ad("ad2").soyad("soyad2").email("email2").telefon("telefon2").adres("adres2");
    }

    public static Musteri getMusteriRandomSampleGenerator() {
        return new Musteri()
            .id(longCount.incrementAndGet())
            .ad(UUID.randomUUID().toString())
            .soyad(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefon(UUID.randomUUID().toString())
            .adres(UUID.randomUUID().toString());
    }
}
