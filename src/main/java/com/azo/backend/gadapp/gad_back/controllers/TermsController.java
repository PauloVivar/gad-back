package com.azo.backend.gadapp.gad_back.controllers;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azo.backend.gadapp.gad_back.models.dto.TermsInteractionDTO;
import com.azo.backend.gadapp.gad_back.models.entities.TermsOfService;
import com.azo.backend.gadapp.gad_back.services.TermsService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/terms")
//@CrossOrigin(origins = "http://localhost:5173/") 
@CrossOrigin(originPatterns = "*")                  //cors solo pruebas
public class TermsController {
  
  // @Autowired
  // private TermsService service;

  private final TermsService service;
  public TermsController(TermsService service) {
      this.service = service;
  }

  // READ (all terms) ok
  @GetMapping
  public ResponseEntity<Iterable<TermsOfService>> getAllTerms() {
      Iterable<TermsOfService> terms = service.getAllTerms();
      return ResponseEntity.ok(terms);
  }

  // READ (single terms by ID) ok
  @GetMapping("/{id}")
  public ResponseEntity<TermsOfService> getTermsById(@PathVariable Long id) {
    Optional<TermsOfService> terms = service.getTermsById(id);
    return terms.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
  }

  // READ (latest terms) ok
  @GetMapping("/latest")
  public ResponseEntity<TermsOfService> getLatestTerms() {
      Optional<TermsOfService> latestTerms = service.getLatestTerms();
      return latestTerms
              .map(terms -> ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(terms))
              .orElse(ResponseEntity.notFound().build());
  }

  // READ (version terms) ok
  @GetMapping("/version/{version}")
  public ResponseEntity<TermsOfService> getTermsByVersion(@PathVariable String version) {
    Optional<TermsOfService> terms = service.getTermsByVersion(version);
    return terms.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
  }

  // POST ok
  @PostMapping
  public ResponseEntity<TermsOfService> createTerms(@RequestBody TermsOfService termsOfService) {
    TermsOfService createdTerms = service.createTerms(termsOfService);
    return new ResponseEntity<>(createdTerms, HttpStatus.CREATED);
  }

  // UPDATE ok
  @PutMapping("/{id}")
  public ResponseEntity<TermsOfService> updateTerms(@PathVariable Long id, @RequestBody TermsOfService termsOfService) {
    try {
      TermsOfService updatedTerms = service.updateTerms(id, termsOfService);
      return ResponseEntity.ok(updatedTerms);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTerms(@PathVariable Long id) {
    try {
      service.deleteTerms(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Record user interaction with terms con @RequestParam ejemplo
  // @PostMapping("/record")
  // public ResponseEntity<Void> recordTermsInteraction(
  //     @RequestParam Long userId,
  //     @RequestParam boolean accepted,
  //     @RequestParam String ipAddress) {
  //   try {
  //     service.recordTermsInteraction(userId, accepted, ipAddress);
  //     return ResponseEntity.ok().build();
  //   } catch (EntityNotFoundException e) {
  //       return ResponseEntity.notFound().build();
  //   }
  // }

  // Record user interaction with terms
  @PostMapping("/record")
  public ResponseEntity<Void> recordTermsInteraction(
    @RequestBody TermsInteractionDTO interactionDTO, 
    HttpServletRequest request) {
    try {
      String ipAddress = getClientIpAddress(request);
      service.recordTermsInteraction(
          interactionDTO.getUserId(),
          interactionDTO.isAccepted(),
          ipAddress
          //interactionDTO.getIpAddress()
      );
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
  }

  private String getClientIpAddress(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
        ipAddress = request.getRemoteAddr();
    }
    return ipAddress != null ? ipAddress : "0.0.0.0"; // Valor por defecto si no se puede obtener la IP
  }

  // Check if user has accepted latest terms
  @GetMapping("/status/{userId}")
  public ResponseEntity<Boolean> checkTermsStatus(@PathVariable Long userId) {
    try {
      boolean hasAccepted = service.hasUserAcceptedLatestTerms(userId);
      return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
                .body(hasAccepted);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
