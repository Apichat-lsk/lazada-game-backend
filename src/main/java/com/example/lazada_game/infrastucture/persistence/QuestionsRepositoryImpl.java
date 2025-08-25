package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Questions;
import com.example.lazada_game.domain.repository.QuestionsRepository;
import com.example.lazada_game.web.dto.CheckAnswerRequest;
import com.example.lazada_game.web.dto.QuestionsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionsRepositoryImpl implements QuestionsRepository {

    private final QuestionsMongoRepository mongoRepository;

    @Override
    public List<Questions> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<Questions> findByDate(QuestionsRequest questionsRequest) {
        LocalDateTime questionDateTime = questionsRequest.getDate();
        ZoneId zoneId = ZoneId.of("Asia/Bangkok");
        LocalDate localDate = questionDateTime.atZone(zoneId).toLocalDate();
        ZonedDateTime startZdt = localDate.minusDays(1).atTime(17, 0).atZone(zoneId);
        Date start = Date.from(startZdt.toInstant());
        ZonedDateTime endZdt = localDate.atTime(16, 59, 59).atZone(zoneId);
        Date end = Date.from(endZdt.toInstant());


        System.out.println("Start Date :" + start);
        System.out.println("End Date :" + end);

        System.out.println("Start Date :" + start);
        System.out.println("End Date :" + end);
        List<Questions> results = mongoRepository.findByCreateAtBetween(start, end);
        for (Questions q : results) {
            q.setAnswer("");
        }
        return results;
    }

    @Override
    public List<Questions> findAnswer(LocalDateTime date) {
        ZoneId zoneId = ZoneId.of("Asia/Bangkok");
        LocalDate localDate = date.atZone(zoneId).toLocalDate();
        ZonedDateTime startZdt = localDate.minusDays(1).atTime(17, 0).atZone(zoneId);
        Date start = Date.from(startZdt.toInstant());
        ZonedDateTime endZdt = localDate.atTime(16, 59, 59).atZone(zoneId);
        Date end = Date.from(endZdt.toInstant());

        System.out.println("Start Date :" + start);
        System.out.println("End Date :" + end);

        System.out.println("Start Date :" + start);
        System.out.println("End Date :" + end);
        List<Questions> results = mongoRepository.findByCreateAtBetween(start, end);
        return results;
    }

    @Override
    public Optional<Questions> findQuestionsById(CheckAnswerRequest request) {
        Optional<Questions> data = mongoRepository.findById(request.getId());
        return data;
    }
}
