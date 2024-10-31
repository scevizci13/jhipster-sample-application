package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Urun;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Urun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UrunRepository extends JpaRepository<Urun, Long> {}
