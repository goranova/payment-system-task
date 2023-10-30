# payment-system-task

The application uses java 17  Oracle db Thymeleaf and Bootstrap
The script for creating user and tables is in
payment-system-task/src/main/resources/static/dbFiles/CreatePaymentSystemUserTable.sql

The transactions and merchants can be imported via post request
The transactions are processed according to their type: 
If Authorize transaction with APPROVED State is imported
    The new Charge transaction is created and processed.
If Charge Transaction with status APPROVED is imported
   Then the  merchant's transaction sum is updated
If Charge Transaction with status REFUND is imported
   Then Refund transaction is created and processed.
If Refund Transaction with APPROVED state is imported
  Then the merchant Transaction sum is updated and 
  referenced Charge transaction is updated with status REFUND.
If Reversal Transaction with APPROVED state is imported
  Then the referenced Authorize transaction is update with status REVERSED. 

The merchant should be in active state when submitting transaction.
There is a cron job for deleting transaction older than an hour. 
The end points: merchants/** and transaction/** are secured, 
only authenticated users can access them.
  
   
