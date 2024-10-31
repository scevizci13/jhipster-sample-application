package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Satis.
 */
@Entity
@Table(name = "satis")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Satis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "satis_tarihi", nullable = false)
    private ZonedDateTime satisTarihi;

    @NotNull
    @Column(name = "miktar", nullable = false)
    private Integer miktar;

    @NotNull
    @Column(name = "toplam_fiyat", precision = 21, scale = 2, nullable = false)
    private BigDecimal toplamFiyat;

    @Column(name = "odeme_durumu")
    private String odemeDurumu;

    @Column(name = "teslimat_adresi")
    private String teslimatAdresi;

    @Column(name = "siparis_notu")
    private String siparisNotu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Musteri musteri;

    @ManyToOne(fetch = FetchType.LAZY)
    private Urun urun;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Satis id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSatisTarihi() {
        return this.satisTarihi;
    }

    public Satis satisTarihi(ZonedDateTime satisTarihi) {
        this.setSatisTarihi(satisTarihi);
        return this;
    }

    public void setSatisTarihi(ZonedDateTime satisTarihi) {
        this.satisTarihi = satisTarihi;
    }

    public Integer getMiktar() {
        return this.miktar;
    }

    public Satis miktar(Integer miktar) {
        this.setMiktar(miktar);
        return this;
    }

    public void setMiktar(Integer miktar) {
        this.miktar = miktar;
    }

    public BigDecimal getToplamFiyat() {
        return this.toplamFiyat;
    }

    public Satis toplamFiyat(BigDecimal toplamFiyat) {
        this.setToplamFiyat(toplamFiyat);
        return this;
    }

    public void setToplamFiyat(BigDecimal toplamFiyat) {
        this.toplamFiyat = toplamFiyat;
    }

    public String getOdemeDurumu() {
        return this.odemeDurumu;
    }

    public Satis odemeDurumu(String odemeDurumu) {
        this.setOdemeDurumu(odemeDurumu);
        return this;
    }

    public void setOdemeDurumu(String odemeDurumu) {
        this.odemeDurumu = odemeDurumu;
    }

    public String getTeslimatAdresi() {
        return this.teslimatAdresi;
    }

    public Satis teslimatAdresi(String teslimatAdresi) {
        this.setTeslimatAdresi(teslimatAdresi);
        return this;
    }

    public void setTeslimatAdresi(String teslimatAdresi) {
        this.teslimatAdresi = teslimatAdresi;
    }

    public String getSiparisNotu() {
        return this.siparisNotu;
    }

    public Satis siparisNotu(String siparisNotu) {
        this.setSiparisNotu(siparisNotu);
        return this;
    }

    public void setSiparisNotu(String siparisNotu) {
        this.siparisNotu = siparisNotu;
    }

    public Musteri getMusteri() {
        return this.musteri;
    }

    public void setMusteri(Musteri musteri) {
        this.musteri = musteri;
    }

    public Satis musteri(Musteri musteri) {
        this.setMusteri(musteri);
        return this;
    }

    public Urun getUrun() {
        return this.urun;
    }

    public void setUrun(Urun urun) {
        this.urun = urun;
    }

    public Satis urun(Urun urun) {
        this.setUrun(urun);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Satis)) {
            return false;
        }
        return getId() != null && getId().equals(((Satis) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Satis{" +
            "id=" + getId() +
            ", satisTarihi='" + getSatisTarihi() + "'" +
            ", miktar=" + getMiktar() +
            ", toplamFiyat=" + getToplamFiyat() +
            ", odemeDurumu='" + getOdemeDurumu() + "'" +
            ", teslimatAdresi='" + getTeslimatAdresi() + "'" +
            ", siparisNotu='" + getSiparisNotu() + "'" +
            "}";
    }
}
