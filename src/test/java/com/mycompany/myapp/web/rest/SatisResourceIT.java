package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SatisAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Satis;
import com.mycompany.myapp.repository.SatisRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link SatisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SatisResourceIT {

    private static final ZonedDateTime DEFAULT_SATIS_TARIHI = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SATIS_TARIHI = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_MIKTAR = 1;
    private static final Integer UPDATED_MIKTAR = 2;

    private static final BigDecimal DEFAULT_TOPLAM_FIYAT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOPLAM_FIYAT = new BigDecimal(2);

    private static final String DEFAULT_ODEME_DURUMU = "AAAAAAAAAA";
    private static final String UPDATED_ODEME_DURUMU = "BBBBBBBBBB";

    private static final String DEFAULT_TESLIMAT_ADRESI = "AAAAAAAAAA";
    private static final String UPDATED_TESLIMAT_ADRESI = "BBBBBBBBBB";

    private static final String DEFAULT_SIPARIS_NOTU = "AAAAAAAAAA";
    private static final String UPDATED_SIPARIS_NOTU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/satis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SatisRepository satisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSatisMockMvc;

    private Satis satis;

    private Satis insertedSatis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Satis createEntity() {
        return new Satis()
            .satisTarihi(DEFAULT_SATIS_TARIHI)
            .miktar(DEFAULT_MIKTAR)
            .toplamFiyat(DEFAULT_TOPLAM_FIYAT)
            .odemeDurumu(DEFAULT_ODEME_DURUMU)
            .teslimatAdresi(DEFAULT_TESLIMAT_ADRESI)
            .siparisNotu(DEFAULT_SIPARIS_NOTU);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Satis createUpdatedEntity() {
        return new Satis()
            .satisTarihi(UPDATED_SATIS_TARIHI)
            .miktar(UPDATED_MIKTAR)
            .toplamFiyat(UPDATED_TOPLAM_FIYAT)
            .odemeDurumu(UPDATED_ODEME_DURUMU)
            .teslimatAdresi(UPDATED_TESLIMAT_ADRESI)
            .siparisNotu(UPDATED_SIPARIS_NOTU);
    }

    @BeforeEach
    public void initTest() {
        satis = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSatis != null) {
            satisRepository.delete(insertedSatis);
            insertedSatis = null;
        }
    }

    @Test
    @Transactional
    void createSatis() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Satis
        var returnedSatis = om.readValue(
            restSatisMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(satis)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Satis.class
        );

        // Validate the Satis in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSatisUpdatableFieldsEquals(returnedSatis, getPersistedSatis(returnedSatis));

        insertedSatis = returnedSatis;
    }

    @Test
    @Transactional
    void createSatisWithExistingId() throws Exception {
        // Create the Satis with an existing ID
        satis.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSatisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(satis)))
            .andExpect(status().isBadRequest());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSatisTarihiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        satis.setSatisTarihi(null);

        // Create the Satis, which fails.

        restSatisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(satis)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMiktarIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        satis.setMiktar(null);

        // Create the Satis, which fails.

        restSatisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(satis)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToplamFiyatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        satis.setToplamFiyat(null);

        // Create the Satis, which fails.

        restSatisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(satis)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSatis() throws Exception {
        // Initialize the database
        insertedSatis = satisRepository.saveAndFlush(satis);

        // Get all the satisList
        restSatisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(satis.getId().intValue())))
            .andExpect(jsonPath("$.[*].satisTarihi").value(hasItem(sameInstant(DEFAULT_SATIS_TARIHI))))
            .andExpect(jsonPath("$.[*].miktar").value(hasItem(DEFAULT_MIKTAR)))
            .andExpect(jsonPath("$.[*].toplamFiyat").value(hasItem(sameNumber(DEFAULT_TOPLAM_FIYAT))))
            .andExpect(jsonPath("$.[*].odemeDurumu").value(hasItem(DEFAULT_ODEME_DURUMU)))
            .andExpect(jsonPath("$.[*].teslimatAdresi").value(hasItem(DEFAULT_TESLIMAT_ADRESI)))
            .andExpect(jsonPath("$.[*].siparisNotu").value(hasItem(DEFAULT_SIPARIS_NOTU)));
    }

    @Test
    @Transactional
    void getSatis() throws Exception {
        // Initialize the database
        insertedSatis = satisRepository.saveAndFlush(satis);

        // Get the satis
        restSatisMockMvc
            .perform(get(ENTITY_API_URL_ID, satis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(satis.getId().intValue()))
            .andExpect(jsonPath("$.satisTarihi").value(sameInstant(DEFAULT_SATIS_TARIHI)))
            .andExpect(jsonPath("$.miktar").value(DEFAULT_MIKTAR))
            .andExpect(jsonPath("$.toplamFiyat").value(sameNumber(DEFAULT_TOPLAM_FIYAT)))
            .andExpect(jsonPath("$.odemeDurumu").value(DEFAULT_ODEME_DURUMU))
            .andExpect(jsonPath("$.teslimatAdresi").value(DEFAULT_TESLIMAT_ADRESI))
            .andExpect(jsonPath("$.siparisNotu").value(DEFAULT_SIPARIS_NOTU));
    }

    @Test
    @Transactional
    void getNonExistingSatis() throws Exception {
        // Get the satis
        restSatisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSatis() throws Exception {
        // Initialize the database
        insertedSatis = satisRepository.saveAndFlush(satis);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the satis
        Satis updatedSatis = satisRepository.findById(satis.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSatis are not directly saved in db
        em.detach(updatedSatis);
        updatedSatis
            .satisTarihi(UPDATED_SATIS_TARIHI)
            .miktar(UPDATED_MIKTAR)
            .toplamFiyat(UPDATED_TOPLAM_FIYAT)
            .odemeDurumu(UPDATED_ODEME_DURUMU)
            .teslimatAdresi(UPDATED_TESLIMAT_ADRESI)
            .siparisNotu(UPDATED_SIPARIS_NOTU);

        restSatisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSatis.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSatis))
            )
            .andExpect(status().isOk());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSatisToMatchAllProperties(updatedSatis);
    }

    @Test
    @Transactional
    void putNonExistingSatis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        satis.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSatisMockMvc
            .perform(put(ENTITY_API_URL_ID, satis.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(satis)))
            .andExpect(status().isBadRequest());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSatis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        satis.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSatisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(satis))
            )
            .andExpect(status().isBadRequest());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSatis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        satis.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSatisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(satis)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSatisWithPatch() throws Exception {
        // Initialize the database
        insertedSatis = satisRepository.saveAndFlush(satis);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the satis using partial update
        Satis partialUpdatedSatis = new Satis();
        partialUpdatedSatis.setId(satis.getId());

        partialUpdatedSatis.satisTarihi(UPDATED_SATIS_TARIHI).odemeDurumu(UPDATED_ODEME_DURUMU).siparisNotu(UPDATED_SIPARIS_NOTU);

        restSatisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSatis.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSatis))
            )
            .andExpect(status().isOk());

        // Validate the Satis in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSatisUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSatis, satis), getPersistedSatis(satis));
    }

    @Test
    @Transactional
    void fullUpdateSatisWithPatch() throws Exception {
        // Initialize the database
        insertedSatis = satisRepository.saveAndFlush(satis);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the satis using partial update
        Satis partialUpdatedSatis = new Satis();
        partialUpdatedSatis.setId(satis.getId());

        partialUpdatedSatis
            .satisTarihi(UPDATED_SATIS_TARIHI)
            .miktar(UPDATED_MIKTAR)
            .toplamFiyat(UPDATED_TOPLAM_FIYAT)
            .odemeDurumu(UPDATED_ODEME_DURUMU)
            .teslimatAdresi(UPDATED_TESLIMAT_ADRESI)
            .siparisNotu(UPDATED_SIPARIS_NOTU);

        restSatisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSatis.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSatis))
            )
            .andExpect(status().isOk());

        // Validate the Satis in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSatisUpdatableFieldsEquals(partialUpdatedSatis, getPersistedSatis(partialUpdatedSatis));
    }

    @Test
    @Transactional
    void patchNonExistingSatis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        satis.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSatisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, satis.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(satis))
            )
            .andExpect(status().isBadRequest());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSatis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        satis.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSatisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(satis))
            )
            .andExpect(status().isBadRequest());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSatis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        satis.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSatisMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(satis)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Satis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSatis() throws Exception {
        // Initialize the database
        insertedSatis = satisRepository.saveAndFlush(satis);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the satis
        restSatisMockMvc
            .perform(delete(ENTITY_API_URL_ID, satis.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return satisRepository.count();
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

    protected Satis getPersistedSatis(Satis satis) {
        return satisRepository.findById(satis.getId()).orElseThrow();
    }

    protected void assertPersistedSatisToMatchAllProperties(Satis expectedSatis) {
        assertSatisAllPropertiesEquals(expectedSatis, getPersistedSatis(expectedSatis));
    }

    protected void assertPersistedSatisToMatchUpdatableProperties(Satis expectedSatis) {
        assertSatisAllUpdatablePropertiesEquals(expectedSatis, getPersistedSatis(expectedSatis));
    }
}
