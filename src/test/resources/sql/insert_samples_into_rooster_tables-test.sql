use bank_data;

-- ----------------- --
-- SAMPLES FOR TESTS --
-- ----------------- --
-- ROOSTER_FILE --
insert into bank_data.rooster_file (file_name, signature, unit_name) values ('test1', 'pouf', 'NATION');

-- ROOSTER_FILE_JOB --
insert into bank_data.rooster_file_job (rooster_file_id, job_instance_id, creation_date)
values ((select rooster_file_id from bank_data.rooster_file where file_name = 'test1' and signature = 'pouf'), 1, now());

insert into bank_data.rooster_file_job (rooster_file_id, job_instance_id, creation_date)
values ((select rooster_file_id from bank_data.rooster_file where file_name = 'test1' and signature = 'pouf'), 2, now());

-- ROOSTER_FILE_INFO --
insert into bank_data.rooster_file_info (rooster_file_id, info_name, info_value)
values ((select rooster_file_id from bank_data.rooster_file where file_name = 'test1' and signature = 'pouf'), "Number of lines", "10001");

insert into bank_data.rooster_file_info (rooster_file_id, info_name, info_value)
values ((select rooster_file_id from bank_data.rooster_file where file_name = 'test1' and signature = 'pouf'), "Number of rejected lines", "100");

commit;
