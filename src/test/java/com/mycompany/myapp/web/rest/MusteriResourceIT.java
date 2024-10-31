package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MusteriAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Musteri;
import com.mycompany.myapp.repository.MusteriRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MusteriResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MusteriResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final String DEFAULT_SOYAD = "AAAAAAAAAA";
    private static final String UPDATED_SOYAD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON = "BBBBBBBBBB";

    private static final String DEFAULT_ADRES = "AAAAAAAAAA";
    private static final String UPDATED_ADRES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_KAYIT_TARIHI = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_KAYIT_TARIHI = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/musteris";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MusteriRepository musteriRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMusteriMockMvc;

    private Musteri musteri;

    private Musteri insertedMusteri;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Musteri createEntity() {
        return new Musteri()
            .ad(DEFAULT_AD)
            .soyad(DEFAULT_SOYAD)
            .email(DEFAULT_EMAIL)
            .telefon(DEFAULT_TELEFON)
            .adres(DEFAULT_ADRES)
            .kayitTarihi(DEFAULT_KAYIT_TARIHI);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Musteri createUpdatedEntity() {
        return new Musteri()
            .ad(UPDATED_AD)
            .soyad(UPDATED_SOYAD)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON)
            .adres(UPDATED_ADRES)
            .kayitTarihi(UPDATED_KAYIT_TARIHI);
    }

    @BeforeEach
    public void initTest() {
        musteri = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMusteri != null) {
            musteriRepository.delete(insertedMusteri);
            insertedMusteri = null;
        }
    }

    @Test
    @Transactional
    void createMusteri() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Musteri
        var returnedMusteri = om.readValue(
            restMusteriMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(musteri)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Musteri.class
        );

        // Validate the Musteri in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMusteriUpdatableFieldsEquals(returnedMusteri, getPersistedMusteri(returnedMusteri));

        insertedMusteri = returnedMusteri;
    }

    @Test
    @Transactional
    void createMusteriWithExistingId() throws Exception {
        // Create the Musteri with an existing ID
        musteri.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusteriMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(musteri)))
            .andExpect(status().isBadRequest());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        musteri.setAd(null);

        // Create the Musteri, which fails.

        restMusteriMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(musteri)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        musteri.setEmail(null);

        // Create the Musteri, which fails.

        restMusteriMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(musteri)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMusteris() throws Exception {
        // Initialize the database
        insertedMusteri = musteriRepository.saveAndFlush(musteri);

        // Get all the musteriList
        restMusteriMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musteri.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].soyad").value(hasItem(DEFAULT_SOYAD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)))
            .andExpect(jsonPath("$.[*].adres").value(hasItem(DEFAULT_ADRES)))
            .andExpect(jsonPath("$.[*].kayitTarihi").value(hasItem(sameInstant(DEFAULT_KAYIT_TARIHI))));
    }

    @Test
    @Transactional
    void getMusteri() throws Exception {
        // Initialize the database
        insertedMusteri = musteriRepository.saveAndFlush(musteri);

        // Get the musteri
        restMusteriMockMvc
            .perform(get(ENTITY_API_URL_ID, musteri.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(musteri.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD))
            .andExpect(jsonPath("$.soyad").value(DEFAULT_SOYAD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefon").value(DEFAULT_TELEFON))
            .andExpect(jsonPath("$.adres").value(DEFAULT_ADRES))
            .andExpect(jsonPath("$.kayitTarihi").value(sameInstant(DEFAULT_KAYIT_TARIHI)));
    }

    @Test
    @Transactional
    void getNonExistingMusteri() throws Exception {
        // Get the musteri
        restMusteriMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMusteri() throws Exception {
        // Initialize the database
        insertedMusteri = musteriRepository.saveAndFlush(musteri);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the musteri
        Musteri updatedMusteri = musteriRepository.findById(musteri.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMusteri are not directly saved in db
        em.detach(updatedMusteri);
        updatedMusteri
            .ad(UPDATED_AD)
            .soyad(UPDATED_SOYAD)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON)
            .adres(UPDATED_ADRES)
            .kayitTarihi(UPDATED_KAYIT_TARIHI);

        restMusteriMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMusteri.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMusteri))
            )
            .andExpect(status().isOk());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMusteriToMatchAllProperties(updatedMusteri);
    }

    @Test
    @Transactional
    void putNonExistingMusteri() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        musteri.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusteriMockMvc
            .perform(put(ENTITY_API_URL_ID, musteri.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(musteri)))
            .andExpect(status().isBadRequest());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMusteri() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        musteri.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusteriMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(musteri))
            )
            .andExpect(status().isBadRequest());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMusteri() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        musteri.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusteriMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(musteri)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMusteriWithPatch() throws Exception {
        // Initialize the database
        insertedMusteri = musteriRepository.saveAndFlush(musteri);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the musteri using partial update
        Musteri partialUpdatedMusteri = new Musteri();
        partialUpdatedMusteri.setId(musteri.getId());

        partialUpdatedMusteri.email(UPDATED_EMAIL).kayitTarihi(UPDATED_KAYIT_TARIHI);

        restMusteriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusteri.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMusteri))
            )
            .andExpect(status().isOk());

        // Validate the Musteri in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMusteriUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMusteri, musteri), getPersistedMusteri(musteri));
    }

    @Test
    @Transactional
    void fullUpdateMusteriWithPatch() throws Exception {
        // Initialize the database
        insertedMusteri = musteriRepository.saveAndFlush(musteri);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the musteri using partial update
        Musteri partialUpdatedMusteri = new Musteri();
        partialUpdatedMusteri.setId(musteri.getId());

        partialUpdatedMusteri
            .ad(UPDATED_AD)
            .soyad(UPDATED_SOYAD)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON)
            .adres(UPDATED_ADRES)
            .kayitTarihi(UPDATED_KAYIT_TARIHI);

        restMusteriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusteri.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMusteri))
            )
            .andExpect(status().isOk());

        // Validate the Musteri in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMusteriUpdatableFieldsEquals(partialUpdatedMusteri, getPersistedMusteri(partialUpdatedMusteri));
    }

    @Test
    @Transactional
    void patchNonExistingMusteri() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        musteri.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusteriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, musteri.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(musteri))
            )
            .andExpect(status().isBadRequest());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMusteri() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        musteri.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusteriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(musteri))
            )
            .andExpect(status().isBadRequest());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMusteri() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        musteri.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusteriMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(musteri)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Musteri in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMusteri() throws Exception {
        // Initialize the database
        insertedMusteri = musteriRepository.saveAndFlush(musteri);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the musteri
        restMusteriMockMvc
            .perform(delete(ENTITY_API_URL_ID, musteri.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return musteriRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Musteri getPersistedMusteri(Musteri musteri) {
        return musteriRepository.findById(musteri.getId()).orElseThrow();
    }

    protected void assertPersistedMusteriToMatchAllProperties(Musteri expectedMusteri) {
        assertMusteriAllPropertiesEquals(expectedMusteri, getPersistedMusteri(expectedMusteri));
    }

    protected void assertPersistedMusteriToMatchUpdatableProperties(Musteri expectedMusteri) {
        assertMusteriAllUpdatablePropertiesEquals(expectedMusteri, getPersistedMusteri(expectedMusteri));
    }
}
