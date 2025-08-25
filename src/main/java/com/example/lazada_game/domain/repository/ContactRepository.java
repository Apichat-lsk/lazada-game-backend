package com.example.lazada_game.domain.repository;

import com.example.lazada_game.domain.model.Contact;
import com.example.lazada_game.web.dto.ContactRequest;
import com.example.lazada_game.web.dto.UserTokenInfo;

public interface ContactRepository {
    Contact createContact(Contact request);
}
