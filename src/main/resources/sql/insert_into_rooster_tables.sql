use bank_data;

insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "ID", 1);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "FIRSTNAME", 2);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "LASTNAME", 3);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "AGE", 4);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "ADDRESS", 5);
insert into bank_data.unit_transco (unit_name, field_name, field_position) values ("NATION", "COUNTRY", 6);

commit;

insert into bank_data.rooster_file (file_name, signature) values ('test', 'pouf');

commit;
