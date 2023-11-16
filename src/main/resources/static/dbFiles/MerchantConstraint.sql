 ALTER TABLE Merchant 
 ADD CONSTRAINT MER_DESCR_STAT_C 
 UNIQUE (description, status);