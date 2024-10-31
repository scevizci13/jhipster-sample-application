package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MusteriTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MusteriTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Musteri.class);
        Musteri musteri1 = getMusteriSample1();
        Musteri musteri2 = new Musteri();
        assertThat(musteri1).isNotEqualTo(musteri2);

        musteri2.setId(musteri1.getId());
        assertThat(musteri1).isEqualTo(musteri2);

        musteri2 = getMusteriSample2();
        assertThat(musteri1).isNotEqualTo(musteri2);
    }
}
