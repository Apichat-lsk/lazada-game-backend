package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.PlayGame;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayGamemongoRepository  extends MongoRepository<PlayGame,String> {
    PlayGame findFirstByUserIdOrderByGameDateDesc(ObjectId userId);
}
