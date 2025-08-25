package com.example.lazada_game.application;


import com.example.lazada_game.domain.model.ActivityLogs;
import com.example.lazada_game.domain.repository.ActivityLogsRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityLogsService {

    private final ActivityLogsRepository activityLogsRepository;

    public void createActivityLogs(ObjectId user_id,String email, String action, String type) {
        ActivityLogs logs = new ActivityLogs();
        logs.setUser_id(user_id);
        logs.setEmail(email);
        logs.setType(type);
        logs.setAction(action);
        logs.setCreateAt(LocalDateTime.now());
        activityLogsRepository.createActivityLogs(logs);
    }

}
