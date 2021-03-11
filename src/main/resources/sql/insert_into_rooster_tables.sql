use bank_data;

insert into bank_data.ref_unit (unit_name, unit_label) values ("NATION", "...");

commit;

insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "ID", 1);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "FIRSTNAME", 2);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "LASTNAME", 3);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "AGE", 4);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "ADDRESS", 5);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "COUNTRY", 6);

commit;

insert into bank_data.rooster_file (file_name, signature, unit_name) values ('test', 'pouf', 'NATION');

commit;

insert into bank_data.ref_check (check_name, check_label) values ('CHECK_001', 'Check on LASTNAME');
insert into bank_data.ref_check (check_name, check_label) values ('CHECK_002', 'Check on COUNTRY');
insert into bank_data.ref_check (check_name, check_label) values ('CHECK_UTF8', 'Check whole line for Utf-8');

insert into bank_data.unit_check (unit_name, check_name) values ('NATION', 'CHECK_001');
insert into bank_data.unit_check (unit_name, check_name) values ('NATION', 'CHECK_002');
insert into bank_data.unit_check (unit_name, check_name, severity) values ('NATION', 'CHECK_UTF8', 'W');

commit;
