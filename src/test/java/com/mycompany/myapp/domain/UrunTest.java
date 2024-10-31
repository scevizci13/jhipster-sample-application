package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.UrunTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UrunTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Urun.class);
        Urun urun1 = getUrunSample1();
        Urun urun2 = new Urun();
        assertThat(urun1).isNotEqualTo(urun2);

        urun2.setId(urun1.getId());
        assertThat(urun1).isEqualTo(urun2);

        urun2 = getUrunSample2();
        assertThat(urun1).isNotEqualTo(urun2);
    }
}
