drop schema if exists bank_data;
create database bank_data;
use bank_data;

CREATE TABLE IF NOT EXISTS account_summary (
  id INT NOT NULL AUTO_INCREMENT ,
  account_number VARCHAR(10) NOT NULL ,
  current_balance DECIMAL(10,2) NOT NULL ,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS transaction (
  id INT NOT NULL AUTO_INCREMENT ,
  timestamp TIMESTAMP NOT NULL ,
  amount DECIMAL(8,2) NOT NULL ,
  account_summary_id INT NOT NULL ,
  PRIMARY KEY (id) ,
  INDEX fk_Transaction_Account_Summary (account_summary_id ASC) ,
  CONSTRAINT fk_Transaction_Account_Summary
    FOREIGN KEY (account_summary_id )
    REFERENCES Account_Summary (id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS unit_transco (
  unit_name 			VARCHAR(20) NOT NULL,
  field_name 			VARCHAR(40) NOT NULL,
  field_position 		INT NOT NULL,
  field_is_mandatory 	BOOLEAN DEFAULT TRUE,
  
  PRIMARY KEY (unit_name, field_name)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS rooster_file (
  rooster_file_id		INT NOT NULL AUTO_INCREMENT,
  file_name				VARCHAR(256) NOT NULL,
  signature 			VARCHAR(256) NOT NULL,
  
  PRIMARY KEY (rooster_file_id),
  UNIQUE (file_name, signature)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS rooster_file_info (
  rooster_file_id		INT NOT NULL AUTO_INCREMENT,
  info_name				VARCHAR(256) NOT NULL,
  info_value			VARCHAR(256) NOT NULL,
  
  PRIMARY KEY (rooster_file_id, info_name),
  
  CONSTRAINT fk_file_info_rooster_file
	FOREIGN KEY (rooster_file_id)
    REFERENCES rooster_file (rooster_file_id)
    ON DELETE no action
    ON UPDATE no action
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS rooster_file_job (
  rooster_file_id		INT NOT NULL AUTO_INCREMENT,
  job_instance_id		BIGINT  NOT NULL,
  creation_date			DATETIME NOT NULL DEFAULT now(),
  
  PRIMARY KEY (rooster_file_id, job_instance_id),
  
  CONSTRAINT fk_file_job_rooster_file
	FOREIGN KEY (rooster_file_id)
    REFERENCES rooster_file (rooster_file_id)
    ON DELETE no action
    ON UPDATE no action,
  
  CONSTRAINT fk_file_job_batch_job_instance
	FOREIGN KEY (job_instance_id)
    REFERENCES spring_batch.batch_job_instance (job_instance_id)
    ON DELETE no action
    ON UPDATE no action
) ENGINE = InnoDB;
