package com.experta.detectart.server.controller;

import static com.experta.detectart.server.controller.UserController.USERS;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.experta.detectart.server.exception.ResourceNotFoundException;
import com.experta.detectart.server.model.Contact;
import com.experta.detectart.server.repository.ContactRepository;
import com.experta.detectart.server.repository.UserRepository;

@RestController
public class ContactController {

    public static final String CONTACTS = "/contacts";

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(USERS + "/{userId}" + CONTACTS)
    public Collection<Contact> getAllContactsByUserId(@PathVariable (value = "userId") final Long userId) {
        return contactRepository.findByUserId(userId);
    }

    @GetMapping(USERS + "/{userId}" + CONTACTS + "/{contactId}")
    public Contact getContactByUserIdAndContactId(
                                    @PathVariable (value = "userId") final Long userId
                                    , @PathVariable (value = "contactId") final Long contactId) {

        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return contactRepository.findByIdAndUserId(contactId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException("ContactId " + contactId + "not found"));
    }

    @PostMapping(USERS + "/{userId}" + CONTACTS)
    public Contact createContact(@PathVariable (value = "userId") final Long userId,
                                 @Valid @RequestBody final Contact contact) {

        return userRepository.findById(userId).map(user -> {

            contact.setUser(user);
            return contactRepository.save(contact);

        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @PutMapping(USERS + "/{userId}" + CONTACTS + "/{contactId}")
    public Contact updateContact(@PathVariable (value = "userId") final Long userId,
                                 @PathVariable (value = "contactId") final Long contactId,
                                 @Valid @RequestBody final Contact contactRequest) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return contactRepository.findById(contactId).map(contact -> {

            contactRequest.copyInto(contact);
            return contactRepository.save(contact);

        }).orElseThrow(() -> new ResourceNotFoundException("ContactId " + contactId + "not found"));
    }

    @DeleteMapping(USERS + "/{userId}" + CONTACTS + "/{contactId}")
    public ResponseEntity<?> deleteContact(@PathVariable (value = "userId") final Long userId,
                              @PathVariable (value = "contactId") final Long contactId) {
        return contactRepository.findByIdAndUserId(contactId, userId).map(contact -> {

            contactRepository.delete(contact);
            return ResponseEntity.ok().body(contact);
        }).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + contactId + " and userId " + userId));
    }
}