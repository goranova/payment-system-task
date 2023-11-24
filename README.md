# payment-system-task

The application uses java 17, Spring Boot, Oracle db Thymeleaf and Bootstrap.
The script for creating user and tables is in
payment-system-task/src/main/resources/static/dbFiles/

The examples files for request and response are in : 
payment-system-task/src/main/resources/static/requestResponse 

The users have ADMIN and MERCHANT roles. The ADMIN can create,edit and delete merchants, 
MERCHANT can list his transactions and import his transactions.

Merchant End points:

List all merchants:GET-> /merchants 

Edit merchant via html form: GEt-> /merchants/{id}

Update merchant: PUT-> /merchants/{id}

Delete merchant: DELETE->/merchants/{id}

Import merchants: POST->/merchants

Upload csv file with merchants: POST->merchants/files


Transaction End Point:

List Authenticated Merchant Transactions: GET->/transactions

Import Transactions: POST->/transactions

The transactions are processed according to their type: 

The TransactionHandlerService.java starts processing the transaction

There is Transaction Validator for transaction properties 
according to the business requirements. 

The authenticated merchant with ACTIVE status is set to the transaction,
otherwise the Missing Merchant exception is thrown. 
This merchant can import and reference only his transactions.

The Authorize transaction is first for both transitions: 

Authorize->Charge->Refund

Authorize->Reversal

Its referenceIdentifier should be null.
The uuid of Authorize transaction should be referenceIdentifier for other 
transactions referenced to it.


Importing Charge transaction:

Should have status APPROVED

And no other referenced Charge transaction with the same referenceIdentifier should exist,
otherwise the exception is thrown.

Should have the same referenceIdentifier as uuid of 
existing Authorize transaction with status APPROVED and the same merchant,
otherwise the transaction is saved with status ERROR.

The Charge transaction should update the Merchant total Transaction sum as adding the given amount.

If the Charge transaction is imported with status ERROR it shouldn't be referenced,
referenceIdentifier is null.


Importing Refund transaction:

Should have status APPROVED

And no other referenced Refund transaction with the same referenceIdentifier should exist,
otherwise the exception is thrown.

Should have the same referenceIdentifier as the referenceIdentifier of
existing Charge transaction with status APPROVED and the same merchant,
otherwise the transaction is saved with status ERROR.

The Refund transaction should update the Merchant total Transaction sum, as subtracting the given amount.

It updates the status of Charge transaction to REFUNDED

If the Refund transaction is imported with status ERROR it shouldn't be referenced,
referenceIdentifier is null.

Importing Reversal Transaction:

Should have status APPROVED

And no other referenced Charge transaction with the same referenceIdentifier should exist,
otherwise the exception is thrown.

Should have the same referenceIdentifier as uuid of
existing Authorize transaction with status APPROVED and the same merchant,
otherwise the transaction is saved with status ERROR.

The Reversal transaction should update the status of referenced Authorize transaction to REVERSED.

If the Reversal transaction is imported with status ERROR it shouldn't be referenced,
referenceIdentifier is null.

There is a cron job for deleting transaction older than an hour. 

Merchants can be deleted only if they don't have transactions.

Merchants can be imported via post request or csv file.

if the merchant with the same description and status exists, the new merchant shouldn't be saved.

There is a constraint MER_DESCR_STAT_C to prevent form duplicate merchants.

  
   
