package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Satis;
import com.mycompany.myapp.repository.SatisRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Satis}.
 */
@Service
@Transactional
public class SatisService {

    private static final Logger LOG = LoggerFactory.getLogger(SatisService.class);

    private final SatisRepository satisRepository;

    public SatisService(SatisRepository satisRepository) {
        this.satisRepository = satisRepository;
    }

    /**
     * Save a satis.
     *
     * @param satis the entity to save.
     * @return the persisted entity.
     */
    public Satis save(Satis satis) {
        LOG.debug("Request to save Satis : {}", satis);
        return satisRepository.save(satis);
    }

    /**
     * Update a satis.
     *
     * @param satis the entity to save.
     * @return the persisted entity.
     */
    public Satis update(Satis satis) {
        LOG.debug("Request to update Satis : {}", satis);
        return satisRepository.save(satis);
    }

    /**
     * Partially update a satis.
     *
     * @param satis the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Satis> partialUpdate(Satis satis) {
        LOG.debug("Request to partially update Satis : {}", satis);

        return satisRepository
            .findById(satis.getId())
            .map(existingSatis -> {
                if (satis.getSatisTarihi() != null) {
                    existingSatis.setSatisTarihi(satis.getSatisTarihi());
                }
                if (satis.getMiktar() != null) {
                    existingSatis.setMiktar(satis.getMiktar());
                }
                if (satis.getToplamFiyat() != null) {
                    existingSatis.setToplamFiyat(satis.getToplamFiyat());
                }
                if (satis.getOdemeDurumu() != null) {
                    existingSatis.setOdemeDurumu(satis.getOdemeDurumu());
                }
                if (satis.getTeslimatAdresi() != null) {
                    existingSatis.setTeslimatAdresi(satis.getTeslimatAdresi());
                }
                if (satis.getSiparisNotu() != null) {
                    existingSatis.setSiparisNotu(satis.getSiparisNotu());
                }

                return existingSatis;
            })
            .map(satisRepository::save);
    }

    /**
     * Get all the satis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Satis> findAll(Pageable pageable) {
        LOG.debug("Request to get all Satis");
        return satisRepository.findAll(pageable);
    }

    /**
     * Get one satis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Satis> findOne(Long id) {
        LOG.debug("Request to get Satis : {}", id);
        return satisRepository.findById(id);
    }

    /**
     * Delete the satis by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Satis : {}", id);
        satisRepository.deleteById(id);
    }
}
