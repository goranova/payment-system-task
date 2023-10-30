alter session set "_ORACLE_SCRIPT"=true;
CREATE USER PAYMENT_SYSTEM IDENTIFIED BY 123456;

ALTER USER PAYMENT_SYSTEM IDENTIFIED BY 123456 ACCOUNT UNLOCK;
		GRANT CONNECT TO PAYMENT_SYSTEM;
		GRANT ALL PRIVILEGES TO PAYMENT_SYSTEM;


CREATE TABLE Transaction(
    uuid VARCHAR(40 CHAR),
    status VARCHAR2(50 CHAR),
    amount FLOAT,
    type VARCHAR2(50 CHAR),
    customer_email VARCHAR2(50 CHAR),
    customer_phone VARCHAR2(50 CHAR),
    reference_id VARCHAR2(40 CHAR),
    merchant_id NUMBER,
    TIMESTMP	TIMESTAMP(6)
    CONSTRAINT Merchant_Transaction_FK
    FOREIGN KEY (merchant_id)
    REFERENCES Merchant (id),
    CONSTRAINT Transaction_Transaction_FK
    FOREIGN KEY (reference_id)
    REFERENCES Transaction (uuid),
    PRIMARY KEY(uuid)
);


CREATE TABLE Merchant(
    id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    description VARCHAR2(255 CHAR),
    email VARCHAR2(50 CHAR),
    status VARCHAR2(50 CHAR),
    total_transaction_sum FLOAT,
    PRIMARY KEY(id)
);

CREATE SEQUENCE S_MERCHANT
    INCREMENT BY 1
     START WITH 1;
     
     
CREATE TABLE user_account(
    id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    user_name VARCHAR2(50 CHAR) not null,
    email VARCHAR2(50 CHAR) not null,
    password VARCHAR2(50 CHAR) not null,
    role VARCHAR2(50 CHAR) not null,
    PRIMARY KEY(id)
);

CREATE SEQUENCE S_user_account
    INCREMENT BY 1
     START WITH 1;