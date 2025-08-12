package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.GameTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface GameTransactionMongoRepository extends MongoRepository<GameTransaction,String> {

    @Query("{ 'user_id': ?0, 'createAt': { $gte: ?1, $lte: ?2 } }")
    List<GameTransaction> findByUserIdAndCreateAtBetween(ObjectId userId, Date start, Date end);

//    List<GameTransaction>  findByUserIdAndCreateAtBetween(ObjectId userId, Date start, Date end);
}
