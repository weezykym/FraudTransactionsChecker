package com.interviewpractice.TransactionsAPI.controller;

import com.interviewpractice.TransactionsAPI.entity.Transactions;
import com.interviewpractice.TransactionsAPI.repository.TransactionRepository;
import com.interviewpractice.TransactionsAPI.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    //1. Endpoint to add a single transaction
    @PostMapping("addTransaction")
    public ResponseEntity<Transactions> addTransaction(@RequestBody Transactions transactions) {
        return ResponseEntity.ok(transactionService.addTransaction(transactions));
    }

    //2. Endpoint to get all transactions in the database
    @GetMapping("allTransactions")
    public ResponseEntity<List<Transactions>> allTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    //3. Endpoint to get all trasactions belonging to a particular student
    @GetMapping("transactions/{studentId}")
    public ResponseEntity<List<Transactions>> getStudentTranactions(@PathVariable String studentId) {
        return ResponseEntity.ok(transactionService.getStudentTransactions(studentId));
    }

    //4. Endpoint to detect fraudulent transactions
    @PostMapping("/fraudulentTransactions")
    public ResponseEntity<List<Transactions>> getFraudulentTrasactions(@RequestBody List<Transactions> transactions) {
        List<Transactions> fraudulentTransactions = transactionService.detectFraudulentTransactions(transactions);
        return ResponseEntity.ok(fraudulentTransactions);
    }
}
