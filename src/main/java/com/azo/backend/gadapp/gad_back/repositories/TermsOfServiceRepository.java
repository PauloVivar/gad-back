package com.azo.backend.gadapp.gad_back.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.azo.backend.gadapp.gad_back.models.entities.TermsOfService;

public interface TermsOfServiceRepository extends CrudRepository<TermsOfService, Long> {

  Optional<TermsOfService> findTopByOrderByEffectiveDateDesc();
  Optional<TermsOfService> findByVersion(String version);
                 
}
