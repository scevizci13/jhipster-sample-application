package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Urun;
import com.mycompany.myapp.repository.UrunRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Urun}.
 */
@Service
@Transactional
public class UrunService {

    private static final Logger LOG = LoggerFactory.getLogger(UrunService.class);

    private final UrunRepository urunRepository;

    public UrunService(UrunRepository urunRepository) {
        this.urunRepository = urunRepository;
    }

    /**
     * Save a urun.
     *
     * @param urun the entity to save.
     * @return the persisted entity.
     */
    public Urun save(Urun urun) {
        LOG.debug("Request to save Urun : {}", urun);
        return urunRepository.save(urun);
    }

    /**
     * Update a urun.
     *
     * @param urun the entity to save.
     * @return the persisted entity.
     */
    public Urun update(Urun urun) {
        LOG.debug("Request to update Urun : {}", urun);
        return urunRepository.save(urun);
    }

    /**
     * Partially update a urun.
     *
     * @param urun the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Urun> partialUpdate(Urun urun) {
        LOG.debug("Request to partially update Urun : {}", urun);

        return urunRepository
            .findById(urun.getId())
            .map(existingUrun -> {
                if (urun.getIsim() != null) {
                    existingUrun.setIsim(urun.getIsim());
                }
                if (urun.getFiyat() != null) {
                    existingUrun.setFiyat(urun.getFiyat());
                }
                if (urun.getLisansAnahtari() != null) {
                    existingUrun.setLisansAnahtari(urun.getLisansAnahtari());
                }
                if (urun.getYenilemeUcreti() != null) {
                    existingUrun.setYenilemeUcreti(urun.getYenilemeUcreti());
                }
                if (urun.getYenilemeTarihi() != null) {
                    existingUrun.setYenilemeTarihi(urun.getYenilemeTarihi());
                }
                if (urun.getAciklama() != null) {
                    existingUrun.setAciklama(urun.getAciklama());
                }
                if (urun.getAktifMi() != null) {
                    existingUrun.setAktifMi(urun.getAktifMi());
                }

                return existingUrun;
            })
            .map(urunRepository::save);
    }

    /**
     * Get all the uruns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Urun> findAll(Pageable pageable) {
        LOG.debug("Request to get all Uruns");
        return urunRepository.findAll(pageable);
    }

    /**
     * Get one urun by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Urun> findOne(Long id) {
        LOG.debug("Request to get Urun : {}", id);
        return urunRepository.findById(id);
    }

    /**
     * Delete the urun by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Urun : {}", id);
        urunRepository.deleteById(id);
    }
}
