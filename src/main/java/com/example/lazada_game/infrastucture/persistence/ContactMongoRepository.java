package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactMongoRepository  extends MongoRepository<Contact,String> {
}
