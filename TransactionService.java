package com.interviewpractice.TransactionsAPI.service;

import com.interviewpractice.TransactionsAPI.entity.Transactions;
import com.interviewpractice.TransactionsAPI.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    //This is the main entry point which ensure all transactions are taken through all the checks
    public List<Transactions> detectFraudulentTransactions(List<Transactions> transactions) {
        List<Transactions> suspiciousTransactions = new ArrayList<>();

        suspiciousTransactions.addAll(detectDuplicateTransactions(transactions));
        suspiciousTransactions.addAll(detectHighFrequencyTransactions(transactions));
        suspiciousTransactions.addAll(detectClusteredPayments(transactions));

        return suspiciousTransactions.stream().distinct().collect(Collectors.toList());
    }

    //1. To detect duplicate transactions
    private List<Transactions> detectDuplicateTransactions(List<Transactions> transactions) {
        List<Transactions> duplicateTransactions = new ArrayList<>();

        for (int i = 0; i < transactions.size(); i++) {
            for (int j = i + 1; j < transactions.size(); j++) {
                Transactions t1 = transactions.get(i);
                Transactions t2 = transactions.get(j);

                if (t1.getAmount().equals(t2.getAmount())
                        && t1.getPaymentMethod().equals(t2.getPaymentMethod())
                        && ChronoUnit.MINUTES.between(t1.getTransactionTime(), t2.getTransactionTime()) <= 10) {

                    duplicateTransactions.add(t1);
                    duplicateTransactions.add(t2);
                }
            }
        }
        return duplicateTransactions;
    }

    //2. To detect unusually high payment frequencies
    private List<Transactions> detectHighFrequencyTransactions(List<Transactions> transactions) {
        List<Transactions> highFrequencyPayments = new ArrayList<>();

        for (Transactions transaction : transactions) {
            LocalDateTime oneHourBefore = transaction.getTransactionTime().minusHours(1);

            List<Transactions> recentTransactions = transactionRepository.findByStudentIdAndTransactionTimeBetween(transaction.getStudentId(),
                    oneHourBefore, transaction.getTransactionTime());

            if (recentTransactions.size() > 3) {
                highFrequencyPayments.addAll(recentTransactions);
            }
        }
        return highFrequencyPayments;
    }
    
    private List<Transactions> detectClusteredPayments(List<Transactions> transactions) {
        List<Transactions> clustered = new ArrayList<>();

        for (Transactions transaction : transactions) {
            List<Transactions> studentTransactions = transactionRepository.findByStudentIdOrderByTransactionTimeAsc(transaction.getStudentId());

            for (int i = 0; i < studentTransactions.size()-1; i++) {
                Transactions current = studentTransactions.get(i);
                Transactions next = studentTransactions.get(i + 1);

                if (!current.getPaymentMethod().equals(next.getPaymentMethod())
                        && ChronoUnit.MINUTES.between(current.getTransactionTime(), next.getTransactionTime()) <=5 ) {

                    BigDecimal thirtyPercentOfBalance = current.getInitialBalance().multiply(new BigDecimal("0.3"));
                    if(current.getAmount().compareTo(thirtyPercentOfBalance) > 0
                            && next.getAmount().compareTo(thirtyPercentOfBalance) > 0) {
                        clustered.add(current);
                        clustered.add(next);
                    }
                }


            }

        }
        return clustered;
    }

    public Transactions addTransaction(Transactions transactions) {
        return transactionRepository.save(transactions);
    }

    public List<Transactions> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transactions> getStudentTransactions(String studentId) {
        return transactionRepository.findByStudentIdOrderByTransactionTimeAsc(studentId);
    }
    

}
