package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Satis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Satis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SatisRepository extends JpaRepository<Satis, Long> {}
