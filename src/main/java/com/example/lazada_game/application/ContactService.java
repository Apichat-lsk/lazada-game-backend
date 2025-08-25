package com.example.lazada_game.application;


import com.example.lazada_game.domain.model.Contact;
import com.example.lazada_game.domain.repository.ContactRepository;
import com.example.lazada_game.web.dto.ContactRequest;
import com.example.lazada_game.web.dto.UserTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository repository;
    private final EmailService emailService;


    public Contact createContact(Contact contact) {
        return repository.createContact(contact);
    }

    public void sendEmail(String toEmail, String title, String description) {
        emailService.sendEmailContact(toEmail, title, description);
    }
}
