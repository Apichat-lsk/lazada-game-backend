package com.example.lazada_game.infrastucture.persistence;


import com.example.lazada_game.domain.model.ActivityLogs;
import com.example.lazada_game.domain.repository.ActivityLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ActivityLogsRepositoryImpl implements ActivityLogsRepository {

    private final ActivityLogsMongoRepository activityLogsMongoRepository;

    @Override
    public void createActivityLogs(ActivityLogs activityLogs) {
        activityLogsMongoRepository.save(activityLogs);
    }
}
