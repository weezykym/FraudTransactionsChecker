package com.interviewpractice.TransactionsAPI.repository;

import com.interviewpractice.TransactionsAPI.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    //1. method is crucial for identifying unusually high payment frequencies
    // by retriving all transactions belonging to a particular student over a certain time period
    List<Transactions> findByStudentIdAndTransactionTimeBetween(String studentId,
                                                                LocalDateTime startTime,
                                                                LocalDateTime endtime);

    //2. method is crucial for detecting duplicate transactions using amount, payment method and a time window
    @Query("SELECT t FROM Transactions t WHERE t.amount= :amount " +
            "AND t.paymentMethod = :paymentMethod " +
            "AND t.transactionTime BETWEEN :startTime AND :endTime")
    List<Transactions> findPotentialDuplicates(@Param("amount")BigDecimal amount,
                                               @Param("paymentMethod") String paymentMethod,
                                               @Param("startTime")LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTIme);
    //3. This method is crucial for detecting payments made by a student ordered by transaction time, this gives a view of payment clustering
    List<Transactions> findByStudentIdOrderByTransactionTimeAsc(String studentId);

}
