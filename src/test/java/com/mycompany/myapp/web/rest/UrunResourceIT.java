package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UrunAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Urun;
import com.mycompany.myapp.repository.UrunRepository;
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
 * Integration tests for the {@link UrunResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UrunResourceIT {

    private static final String DEFAULT_ISIM = "AAAAAAAAAA";
    private static final String UPDATED_ISIM = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FIYAT = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIYAT = new BigDecimal(2);

    private static final String DEFAULT_LISANS_ANAHTARI = "AAAAAAAAAA";
    private static final String UPDATED_LISANS_ANAHTARI = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_YENILEME_UCRETI = new BigDecimal(1);
    private static final BigDecimal UPDATED_YENILEME_UCRETI = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_YENILEME_TARIHI = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_YENILEME_TARIHI = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AKTIF_MI = false;
    private static final Boolean UPDATED_AKTIF_MI = true;

    private static final String ENTITY_API_URL = "/api/uruns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UrunRepository urunRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUrunMockMvc;

    private Urun urun;

    private Urun insertedUrun;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Urun createEntity() {
        return new Urun()
            .isim(DEFAULT_ISIM)
            .fiyat(DEFAULT_FIYAT)
            .lisansAnahtari(DEFAULT_LISANS_ANAHTARI)
            .yenilemeUcreti(DEFAULT_YENILEME_UCRETI)
            .yenilemeTarihi(DEFAULT_YENILEME_TARIHI)
            .aciklama(DEFAULT_ACIKLAMA)
            .aktifMi(DEFAULT_AKTIF_MI);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Urun createUpdatedEntity() {
        return new Urun()
            .isim(UPDATED_ISIM)
            .fiyat(UPDATED_FIYAT)
            .lisansAnahtari(UPDATED_LISANS_ANAHTARI)
            .yenilemeUcreti(UPDATED_YENILEME_UCRETI)
            .yenilemeTarihi(UPDATED_YENILEME_TARIHI)
            .aciklama(UPDATED_ACIKLAMA)
            .aktifMi(UPDATED_AKTIF_MI);
    }

    @BeforeEach
    public void initTest() {
        urun = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUrun != null) {
            urunRepository.delete(insertedUrun);
            insertedUrun = null;
        }
    }

    @Test
    @Transactional
    void createUrun() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Urun
        var returnedUrun = om.readValue(
            restUrunMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urun)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Urun.class
        );

        // Validate the Urun in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUrunUpdatableFieldsEquals(returnedUrun, getPersistedUrun(returnedUrun));

        insertedUrun = returnedUrun;
    }

    @Test
    @Transactional
    void createUrunWithExistingId() throws Exception {
        // Create the Urun with an existing ID
        urun.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUrunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urun)))
            .andExpect(status().isBadRequest());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsimIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        urun.setIsim(null);

        // Create the Urun, which fails.

        restUrunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urun)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiyatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        urun.setFiyat(null);

        // Create the Urun, which fails.

        restUrunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urun)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUruns() throws Exception {
        // Initialize the database
        insertedUrun = urunRepository.saveAndFlush(urun);

        // Get all the urunList
        restUrunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urun.getId().intValue())))
            .andExpect(jsonPath("$.[*].isim").value(hasItem(DEFAULT_ISIM)))
            .andExpect(jsonPath("$.[*].fiyat").value(hasItem(sameNumber(DEFAULT_FIYAT))))
            .andExpect(jsonPath("$.[*].lisansAnahtari").value(hasItem(DEFAULT_LISANS_ANAHTARI)))
            .andExpect(jsonPath("$.[*].yenilemeUcreti").value(hasItem(sameNumber(DEFAULT_YENILEME_UCRETI))))
            .andExpect(jsonPath("$.[*].yenilemeTarihi").value(hasItem(sameInstant(DEFAULT_YENILEME_TARIHI))))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].aktifMi").value(hasItem(DEFAULT_AKTIF_MI.booleanValue())));
    }

    @Test
    @Transactional
    void getUrun() throws Exception {
        // Initialize the database
        insertedUrun = urunRepository.saveAndFlush(urun);

        // Get the urun
        restUrunMockMvc
            .perform(get(ENTITY_API_URL_ID, urun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(urun.getId().intValue()))
            .andExpect(jsonPath("$.isim").value(DEFAULT_ISIM))
            .andExpect(jsonPath("$.fiyat").value(sameNumber(DEFAULT_FIYAT)))
            .andExpect(jsonPath("$.lisansAnahtari").value(DEFAULT_LISANS_ANAHTARI))
            .andExpect(jsonPath("$.yenilemeUcreti").value(sameNumber(DEFAULT_YENILEME_UCRETI)))
            .andExpect(jsonPath("$.yenilemeTarihi").value(sameInstant(DEFAULT_YENILEME_TARIHI)))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA))
            .andExpect(jsonPath("$.aktifMi").value(DEFAULT_AKTIF_MI.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUrun() throws Exception {
        // Get the urun
        restUrunMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUrun() throws Exception {
        // Initialize the database
        insertedUrun = urunRepository.saveAndFlush(urun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the urun
        Urun updatedUrun = urunRepository.findById(urun.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUrun are not directly saved in db
        em.detach(updatedUrun);
        updatedUrun
            .isim(UPDATED_ISIM)
            .fiyat(UPDATED_FIYAT)
            .lisansAnahtari(UPDATED_LISANS_ANAHTARI)
            .yenilemeUcreti(UPDATED_YENILEME_UCRETI)
            .yenilemeTarihi(UPDATED_YENILEME_TARIHI)
            .aciklama(UPDATED_ACIKLAMA)
            .aktifMi(UPDATED_AKTIF_MI);

        restUrunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUrun.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUrun))
            )
            .andExpect(status().isOk());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUrunToMatchAllProperties(updatedUrun);
    }

    @Test
    @Transactional
    void putNonExistingUrun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urun.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrunMockMvc
            .perform(put(ENTITY_API_URL_ID, urun.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urun)))
            .andExpect(status().isBadRequest());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUrun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urun.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(urun))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUrun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urun.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrunMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(urun)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUrunWithPatch() throws Exception {
        // Initialize the database
        insertedUrun = urunRepository.saveAndFlush(urun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the urun using partial update
        Urun partialUpdatedUrun = new Urun();
        partialUpdatedUrun.setId(urun.getId());

        partialUpdatedUrun.yenilemeUcreti(UPDATED_YENILEME_UCRETI);

        restUrunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrun.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUrun))
            )
            .andExpect(status().isOk());

        // Validate the Urun in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUrunUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUrun, urun), getPersistedUrun(urun));
    }

    @Test
    @Transactional
    void fullUpdateUrunWithPatch() throws Exception {
        // Initialize the database
        insertedUrun = urunRepository.saveAndFlush(urun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the urun using partial update
        Urun partialUpdatedUrun = new Urun();
        partialUpdatedUrun.setId(urun.getId());

        partialUpdatedUrun
            .isim(UPDATED_ISIM)
            .fiyat(UPDATED_FIYAT)
            .lisansAnahtari(UPDATED_LISANS_ANAHTARI)
            .yenilemeUcreti(UPDATED_YENILEME_UCRETI)
            .yenilemeTarihi(UPDATED_YENILEME_TARIHI)
            .aciklama(UPDATED_ACIKLAMA)
            .aktifMi(UPDATED_AKTIF_MI);

        restUrunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrun.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUrun))
            )
            .andExpect(status().isOk());

        // Validate the Urun in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUrunUpdatableFieldsEquals(partialUpdatedUrun, getPersistedUrun(partialUpdatedUrun));
    }

    @Test
    @Transactional
    void patchNonExistingUrun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urun.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrunMockMvc
            .perform(patch(ENTITY_API_URL_ID, urun.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(urun)))
            .andExpect(status().isBadRequest());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUrun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urun.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(urun))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUrun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        urun.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrunMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(urun)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Urun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUrun() throws Exception {
        // Initialize the database
        insertedUrun = urunRepository.saveAndFlush(urun);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the urun
        restUrunMockMvc
            .perform(delete(ENTITY_API_URL_ID, urun.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return urunRepository.count();
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

    protected Urun getPersistedUrun(Urun urun) {
        return urunRepository.findById(urun.getId()).orElseThrow();
    }

    protected void assertPersistedUrunToMatchAllProperties(Urun expectedUrun) {
        assertUrunAllPropertiesEquals(expectedUrun, getPersistedUrun(expectedUrun));
    }

    protected void assertPersistedUrunToMatchUpdatableProperties(Urun expectedUrun) {
        assertUrunAllUpdatablePropertiesEquals(expectedUrun, getPersistedUrun(expectedUrun));
    }
}
