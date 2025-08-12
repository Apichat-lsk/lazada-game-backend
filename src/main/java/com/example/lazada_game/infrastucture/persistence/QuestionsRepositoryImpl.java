package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Questions;
import com.example.lazada_game.domain.repository.QuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionsRepositoryImpl implements QuestionsRepository {

    private final QuestionsMongoRepository mongoRepository;

    @Override
    public List<Questions> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<Questions> findByDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.of("Asia/Bangkok");
        LocalDate today = LocalDate.now(zoneId);
        ZonedDateTime startZdt = today.minusDays(1).atTime(17, 0).atZone(zoneId);
        Date start = Date.from(startZdt.toInstant());
        ZonedDateTime endZdt = today.atTime(16, 59, 59).atZone(zoneId);
        Date end = Date.from(endZdt.toInstant());

        System.out.println("Start Date :" + start);
        System.out.println("End Date :" + end);

        System.out.println("Start Date :" + start);
        System.out.println("End Date :" + end);
        List<Questions> results = mongoRepository.findByCreateAtBetween(start, end);
        return results;
    }
}
