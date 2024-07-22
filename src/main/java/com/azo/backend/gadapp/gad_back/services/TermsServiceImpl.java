package com.azo.backend.gadapp.gad_back.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azo.backend.gadapp.gad_back.models.entities.TermsAcceptance;
import com.azo.backend.gadapp.gad_back.models.entities.TermsOfService;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.repositories.TermsAcceptanceRepository;
import com.azo.backend.gadapp.gad_back.repositories.TermsOfServiceRepository;
import com.azo.backend.gadapp.gad_back.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TermsServiceImpl implements TermsService {

  @Autowired
  private TermsOfServiceRepository termsRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TermsAcceptanceRepository termsAcceptanceRepository;

  @Override
  @Transactional(readOnly = true)
  public Iterable<TermsOfService> getAllTerms() {
    return termsRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TermsOfService> getTermsById(Long id) {
    return termsRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TermsOfService> getLatestTerms() {
    return termsRepository.findTopByOrderByEffectiveDateDesc();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TermsOfService> getTermsByVersion(String version) {
    return termsRepository.findByVersion(version);
  }

  @Override
  @Transactional
  public TermsOfService createTerms(TermsOfService termsOfService) {
    return termsRepository.save(termsOfService);
  }

  @Override
  @Transactional
  public TermsOfService updateTerms(Long id, TermsOfService termsOfService) {
    Optional<TermsOfService> o = termsRepository.findById(id);
    if (o.isPresent()) {
        TermsOfService updatedTerms = o.orElseThrow();
        updatedTerms.setVersion(termsOfService.getVersion());
        updatedTerms.setContent(termsOfService.getContent());
        updatedTerms.setEffectiveDate(termsOfService.getEffectiveDate());
        return termsRepository.save(updatedTerms);
    } else {
        throw new EntityNotFoundException("Términos no encontrados con id: " + id);
    }
  }

  @Override
  @Transactional
  public void deleteTerms(Long id) {
    termsRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void recordTermsInteraction(Long userId, boolean accepted, String ipAddress) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    TermsOfService latestTerms = getLatestTerms()
        .orElseThrow(() -> new EntityNotFoundException("No se encontraron términos"));
    
    //busca exactamente el registro de aceptación para el usuario y los términos específicos, 
    //lo que es ideal para la lógica de actualización o creación de nuevos registros.
    Optional<TermsAcceptance> existingAcceptance = termsAcceptanceRepository
        .findByUserAndTermsOfService(user, latestTerms);
    
    if (existingAcceptance.isPresent()) {
      TermsAcceptance acceptance = existingAcceptance.get();
      acceptance.setAccepted(accepted);
      acceptance.setIpAddress(ipAddress);
      // No necesitamos setAcceptanceDate aquí, se actualizará automáticamente
    } else {
      TermsAcceptance acceptance = new TermsAcceptance();
      acceptance.setUser(user);
      acceptance.setTermsOfService(latestTerms);
      acceptance.setAccepted(accepted);
      acceptance.setIpAddress(ipAddress);
      termsAcceptanceRepository.save(acceptance);
    }
  }

  @Override
  @Transactional
  public boolean hasUserAcceptedLatestTerms(Long userId) {
    Optional<User> ou = userRepository.findById(userId);
    Optional<TermsOfService> latestTermsOpt = getLatestTerms();
    
    if (ou.isPresent() && latestTermsOpt.isPresent()) {
        User user = ou.get();
        TermsOfService latestTerms = latestTermsOpt.get();
        
        Optional<TermsAcceptance> latestAcceptance = termsAcceptanceRepository
            .findTopByUserAndTermsOfServiceOrderByAcceptanceDateDesc(user, latestTerms);
        
        return latestAcceptance.map(TermsAcceptance::isAccepted).orElse(false);
    } else {
        return false;
    }
  }

}
