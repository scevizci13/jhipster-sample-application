package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MusteriTestSamples.*;
import static com.mycompany.myapp.domain.SatisTestSamples.*;
import static com.mycompany.myapp.domain.UrunTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SatisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Satis.class);
        Satis satis1 = getSatisSample1();
        Satis satis2 = new Satis();
        assertThat(satis1).isNotEqualTo(satis2);

        satis2.setId(satis1.getId());
        assertThat(satis1).isEqualTo(satis2);

        satis2 = getSatisSample2();
        assertThat(satis1).isNotEqualTo(satis2);
    }

    @Test
    void musteriTest() {
        Satis satis = getSatisRandomSampleGenerator();
        Musteri musteriBack = getMusteriRandomSampleGenerator();

        satis.setMusteri(musteriBack);
        assertThat(satis.getMusteri()).isEqualTo(musteriBack);

        satis.musteri(null);
        assertThat(satis.getMusteri()).isNull();
    }

    @Test
    void urunTest() {
        Satis satis = getSatisRandomSampleGenerator();
        Urun urunBack = getUrunRandomSampleGenerator();

        satis.setUrun(urunBack);
        assertThat(satis.getUrun()).isEqualTo(urunBack);

        satis.urun(null);
        assertThat(satis.getUrun()).isNull();
    }
}
