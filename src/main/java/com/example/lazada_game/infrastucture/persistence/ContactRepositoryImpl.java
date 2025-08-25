package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Contact;
import com.example.lazada_game.domain.repository.ContactRepository;
import com.example.lazada_game.web.dto.ContactRequest;
import com.example.lazada_game.web.dto.UserTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ContactRepositoryImpl implements ContactRepository {

    private final ContactMongoRepository mongoRepository;

    @Override
    public Contact createContact(Contact request) {
        Contact newContact = new Contact();
//        newContact.setUser_id(userTokenInfo.getUserId());
        newContact.setEmail(request.getEmail());
        newContact.setTitle(request.getTitle());
        newContact.setDescription(request.getDescription());
        newContact.setCreateAt(LocalDateTime.now());
      Contact data=  mongoRepository.save(newContact);
        return data;
    }
}
