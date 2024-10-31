package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Musteri;
import com.mycompany.myapp.repository.MusteriRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Musteri}.
 */
@Service
@Transactional
public class MusteriService {

    private static final Logger LOG = LoggerFactory.getLogger(MusteriService.class);

    private final MusteriRepository musteriRepository;

    public MusteriService(MusteriRepository musteriRepository) {
        this.musteriRepository = musteriRepository;
    }

    /**
     * Save a musteri.
     *
     * @param musteri the entity to save.
     * @return the persisted entity.
     */
    public Musteri save(Musteri musteri) {
        LOG.debug("Request to save Musteri : {}", musteri);
        return musteriRepository.save(musteri);
    }

    /**
     * Update a musteri.
     *
     * @param musteri the entity to save.
     * @return the persisted entity.
     */
    public Musteri update(Musteri musteri) {
        LOG.debug("Request to update Musteri : {}", musteri);
        return musteriRepository.save(musteri);
    }

    /**
     * Partially update a musteri.
     *
     * @param musteri the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Musteri> partialUpdate(Musteri musteri) {
        LOG.debug("Request to partially update Musteri : {}", musteri);

        return musteriRepository
            .findById(musteri.getId())
            .map(existingMusteri -> {
                if (musteri.getAd() != null) {
                    existingMusteri.setAd(musteri.getAd());
                }
                if (musteri.getSoyad() != null) {
                    existingMusteri.setSoyad(musteri.getSoyad());
                }
                if (musteri.getEmail() != null) {
                    existingMusteri.setEmail(musteri.getEmail());
                }
                if (musteri.getTelefon() != null) {
                    existingMusteri.setTelefon(musteri.getTelefon());
                }
                if (musteri.getAdres() != null) {
                    existingMusteri.setAdres(musteri.getAdres());
                }
                if (musteri.getKayitTarihi() != null) {
                    existingMusteri.setKayitTarihi(musteri.getKayitTarihi());
                }

                return existingMusteri;
            })
            .map(musteriRepository::save);
    }

    /**
     * Get all the musteris.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Musteri> findAll(Pageable pageable) {
        LOG.debug("Request to get all Musteris");
        return musteriRepository.findAll(pageable);
    }

    /**
     * Get one musteri by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Musteri> findOne(Long id) {
        LOG.debug("Request to get Musteri : {}", id);
        return musteriRepository.findById(id);
    }

    /**
     * Delete the musteri by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Musteri : {}", id);
        musteriRepository.deleteById(id);
    }
}
