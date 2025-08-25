package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.repository.BoardRepository;
import com.example.lazada_game.web.dto.BoardRequest;
import com.example.lazada_game.web.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<BoardResponse> findAllBoard(BoardRequest request) {
        LocalDate requestLocalDate = request.getDate().toInstant()
                .atZone(ZoneId.of("Asia/Bangkok"))
                .toLocalDate();

        ZoneId zoneBangkok = ZoneId.of("Asia/Bangkok");
        ZonedDateTime startBangkok = requestLocalDate.atStartOfDay(zoneBangkok);
        ZonedDateTime endBangkok = requestLocalDate.plusDays(1)
                .atStartOfDay(zoneBangkok)
                .minusNanos(1);

        ZonedDateTime startUTC = startBangkok.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endBangkok.withZoneSameInstant(ZoneId.of("UTC"));

        Date startDate = Date.from(startUTC.toInstant());
        Date endDate = Date.from(endUTC.toInstant());

        System.out.println("Start UTC: " + startUTC);
        System.out.println("End UTC:   " + endUTC);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date:   " + endDate);
        Aggregation agg = newAggregation(
                match(Criteria.where("questionsDate").gte(startDate)
                        .lte(endDate)),
                group("user_id").sum("score").as("score"),
                lookup("users", "_id", "_id", "userInfo"),
                unwind("userInfo"),
                project()
                        .and("_id").as("user_id")
                        .and("score").as("score")
                        .and("userInfo.username").as("username")
                        .and("userInfo.email").as("email"),
                sort(Sort.by(Sort.Direction.DESC, "score")),
                limit(400)
        );
        List<Document> documents = mongoTemplate.aggregate(agg, "game_transaction", Document.class).getMappedResults();

        return documents.stream().map(doc -> {
            BoardResponse response = new BoardResponse();
            response.setUser_id((ObjectId) doc.get("user_id"));
            response.setScore(doc.getInteger("score", 0));
            response.setUsername(doc.getString("username"));
            response.setEmail(doc.getString("email"));
            return response;
        }).collect(Collectors.toList());
    }
}
