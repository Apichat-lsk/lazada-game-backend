package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Questions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface QuestionsMongoRepository extends MongoRepository<Questions,String> {

    List<Questions> findByCreateAtBetween(Date start, Date end);

}
