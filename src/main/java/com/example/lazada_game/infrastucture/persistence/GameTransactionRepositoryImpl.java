package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.GameTransaction;
import com.example.lazada_game.domain.repository.GameTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GameTransactionRepositoryImpl implements GameTransactionRepository {

    private final GameTransactionMongoRepository mongoRepository;

    @Override
    public void createGameTransaction(List<GameTransaction> gameTransaction) {
        mongoRepository.saveAll(gameTransaction);
    }

    @Override
    public GameTransaction findByDate(GameTransaction user, LocalDate localDate) {
        ZoneId zoneBangkok = ZoneId.of("Asia/Bangkok");
        ZonedDateTime startBangkok = localDate.atStartOfDay(zoneBangkok);
        ZonedDateTime endBangkok = localDate.plusDays(1).atStartOfDay(zoneBangkok).minusNanos(1);

        ZonedDateTime startUTC = startBangkok.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endBangkok.withZoneSameInstant(ZoneId.of("UTC"));
        Date startDate = Date.from(startUTC.toInstant());
        Date endDate = Date.from(endUTC.toInstant());
        System.out.println("Start UTC: " + startUTC);
        System.out.println("End UTC:   " + endUTC);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date:   " + endDate);
        List<GameTransaction> dataList = mongoRepository.findByUserIdAndCreateAtBetween(user.getUserId(), startDate, endDate);
        System.out.println("dataList :" + dataList);
        if (dataList.isEmpty()) {
            System.out.println("No records found");
            return null;
        }
        return dataList.get(0);
    }
}
