package com.azo.backend.gadapp.gad_back.services;

import java.util.Optional;
import com.azo.backend.gadapp.gad_back.models.entities.TermsOfService;

public interface TermsService {

  Iterable<TermsOfService> getAllTerms();

  Optional<TermsOfService> getTermsById(Long id);

  Optional<TermsOfService> getLatestTerms();

  Optional<TermsOfService> getTermsByVersion(String version);

  TermsOfService createTerms(TermsOfService termsOfService);

  TermsOfService updateTerms(Long id, TermsOfService termsOfService);

  void deleteTerms(Long id);

  void recordTermsInteraction(Long userId, boolean accepted, String ipAddress);

  boolean hasUserAcceptedLatestTerms(Long userId);

}
