package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Musteri.
 */
@Entity
@Table(name = "musteri")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Musteri implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ad", nullable = false)
    private String ad;

    @Column(name = "soyad")
    private String soyad;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "adres")
    private String adres;

    @Column(name = "kayit_tarihi")
    private ZonedDateTime kayitTarihi;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Musteri id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return this.ad;
    }

    public Musteri ad(String ad) {
        this.setAd(ad);
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return this.soyad;
    }

    public Musteri soyad(String soyad) {
        this.setSoyad(soyad);
        return this;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEmail() {
        return this.email;
    }

    public Musteri email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public Musteri telefon(String telefon) {
        this.setTelefon(telefon);
        return this;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdres() {
        return this.adres;
    }

    public Musteri adres(String adres) {
        this.setAdres(adres);
        return this;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public ZonedDateTime getKayitTarihi() {
        return this.kayitTarihi;
    }

    public Musteri kayitTarihi(ZonedDateTime kayitTarihi) {
        this.setKayitTarihi(kayitTarihi);
        return this;
    }

    public void setKayitTarihi(ZonedDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Musteri)) {
            return false;
        }
        return getId() != null && getId().equals(((Musteri) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Musteri{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            ", soyad='" + getSoyad() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefon='" + getTelefon() + "'" +
            ", adres='" + getAdres() + "'" +
            ", kayitTarihi='" + getKayitTarihi() + "'" +
            "}";
    }
}
