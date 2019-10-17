package com.experta.detectart.server.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.experta.detectart.server.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Collection<Contact> findByUserId(Long userId);
    Optional<Contact> findByIdAndUserId(Long id, Long userId);
}
