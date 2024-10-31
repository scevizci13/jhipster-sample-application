package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AssertUtils.bigDecimalCompareTo;
import static com.mycompany.myapp.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class UrunAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUrunAllPropertiesEquals(Urun expected, Urun actual) {
        assertUrunAutoGeneratedPropertiesEquals(expected, actual);
        assertUrunAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUrunAllUpdatablePropertiesEquals(Urun expected, Urun actual) {
        assertUrunUpdatableFieldsEquals(expected, actual);
        assertUrunUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUrunAutoGeneratedPropertiesEquals(Urun expected, Urun actual) {
        assertThat(expected)
            .as("Verify Urun auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUrunUpdatableFieldsEquals(Urun expected, Urun actual) {
        assertThat(expected)
            .as("Verify Urun relevant properties")
            .satisfies(e -> assertThat(e.getIsim()).as("check isim").isEqualTo(actual.getIsim()))
            .satisfies(e -> assertThat(e.getFiyat()).as("check fiyat").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getFiyat()))
            .satisfies(e -> assertThat(e.getLisansAnahtari()).as("check lisansAnahtari").isEqualTo(actual.getLisansAnahtari()))
            .satisfies(e ->
                assertThat(e.getYenilemeUcreti())
                    .as("check yenilemeUcreti")
                    .usingComparator(bigDecimalCompareTo)
                    .isEqualTo(actual.getYenilemeUcreti())
            )
            .satisfies(e ->
                assertThat(e.getYenilemeTarihi())
                    .as("check yenilemeTarihi")
                    .usingComparator(zonedDataTimeSameInstant)
                    .isEqualTo(actual.getYenilemeTarihi())
            )
            .satisfies(e -> assertThat(e.getAciklama()).as("check aciklama").isEqualTo(actual.getAciklama()))
            .satisfies(e -> assertThat(e.getAktifMi()).as("check aktifMi").isEqualTo(actual.getAktifMi()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUrunUpdatableRelationshipsEquals(Urun expected, Urun actual) {
        // empty method
    }
}
