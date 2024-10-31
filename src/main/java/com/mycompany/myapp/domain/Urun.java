package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Urun.
 */
@Entity
@Table(name = "urun")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Urun implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "isim", nullable = false)
    private String isim;

    @NotNull
    @Column(name = "fiyat", precision = 21, scale = 2, nullable = false)
    private BigDecimal fiyat;

    @Column(name = "lisans_anahtari", unique = true)
    private String lisansAnahtari;

    @Column(name = "yenileme_ucreti", precision = 21, scale = 2)
    private BigDecimal yenilemeUcreti;

    @Column(name = "yenileme_tarihi")
    private ZonedDateTime yenilemeTarihi;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "aktif_mi")
    private Boolean aktifMi;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Urun id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsim() {
        return this.isim;
    }

    public Urun isim(String isim) {
        this.setIsim(isim);
        return this;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public BigDecimal getFiyat() {
        return this.fiyat;
    }

    public Urun fiyat(BigDecimal fiyat) {
        this.setFiyat(fiyat);
        return this;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }

    public String getLisansAnahtari() {
        return this.lisansAnahtari;
    }

    public Urun lisansAnahtari(String lisansAnahtari) {
        this.setLisansAnahtari(lisansAnahtari);
        return this;
    }

    public void setLisansAnahtari(String lisansAnahtari) {
        this.lisansAnahtari = lisansAnahtari;
    }

    public BigDecimal getYenilemeUcreti() {
        return this.yenilemeUcreti;
    }

    public Urun yenilemeUcreti(BigDecimal yenilemeUcreti) {
        this.setYenilemeUcreti(yenilemeUcreti);
        return this;
    }

    public void setYenilemeUcreti(BigDecimal yenilemeUcreti) {
        this.yenilemeUcreti = yenilemeUcreti;
    }

    public ZonedDateTime getYenilemeTarihi() {
        return this.yenilemeTarihi;
    }

    public Urun yenilemeTarihi(ZonedDateTime yenilemeTarihi) {
        this.setYenilemeTarihi(yenilemeTarihi);
        return this;
    }

    public void setYenilemeTarihi(ZonedDateTime yenilemeTarihi) {
        this.yenilemeTarihi = yenilemeTarihi;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Urun aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Boolean getAktifMi() {
        return this.aktifMi;
    }

    public Urun aktifMi(Boolean aktifMi) {
        this.setAktifMi(aktifMi);
        return this;
    }

    public void setAktifMi(Boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Urun)) {
            return false;
        }
        return getId() != null && getId().equals(((Urun) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Urun{" +
            "id=" + getId() +
            ", isim='" + getIsim() + "'" +
            ", fiyat=" + getFiyat() +
            ", lisansAnahtari='" + getLisansAnahtari() + "'" +
            ", yenilemeUcreti=" + getYenilemeUcreti() +
            ", yenilemeTarihi='" + getYenilemeTarihi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", aktifMi='" + getAktifMi() + "'" +
            "}";
    }
}
