package com.azo.backend.gadapp.gad_back.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.azo.backend.gadapp.gad_back.models.entities.TermsAcceptance;
import com.azo.backend.gadapp.gad_back.models.entities.TermsOfService;
import com.azo.backend.gadapp.gad_back.models.entities.User;

public interface TermsAcceptanceRepository extends CrudRepository<TermsAcceptance, Long> {

  Optional<TermsAcceptance> findTopByUserAndTermsOfServiceOrderByAcceptanceDateDesc(
    User user, 
    TermsOfService termsOfService);
  
  //test
  Optional<TermsAcceptance> findByUserAndTermsOfService(User user, TermsOfService termsOfService);
    
}
