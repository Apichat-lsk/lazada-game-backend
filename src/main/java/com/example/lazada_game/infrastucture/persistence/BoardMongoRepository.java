package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardMongoRepository extends MongoRepository<Board,String> {




}
