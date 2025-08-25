package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.ActivityLogs;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityLogsMongoRepository extends MongoRepository<ActivityLogs,String> {
}
