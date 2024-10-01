# FraudTransactionsChecker

#Spring Boot application that processes a batch of school fee transactions and identifies fraudulent transactions using an efficient, non-trivial algorithm. 

 

#Task Details 

#You are tasked with creating a basic API service in Spring Boot that takes a list of fee payment transactions and returns a list of suspicious transactions based on the following criteria: 

#Duplicate Transactions: 

#If two transactions have the same amount, payment method, and are within 10 minutes of each other, one of them should be flagged as suspicious. 

#Unusually High Payment Frequency: 

#If a student makes more than 3 payments within an hour, the system should flag all those payments as suspicious. 

#Algorithm Challenge - Payment Clustering: 

#Implement a custom algorithm to detect clusters of payments made by the same payer (same student ID) where the payments are unusually close together in time but under different payment methods (e.g., one through mobile #money and another through a bank transfer). These payments should be flagged if they occur within 5 minutes of each other and are over 30% of the student's previous balance. 
