
drop table if exists ers_reimbursements; 
drop table if exists ers_users cascade; 
drop table if exists ers_user_roles; 
drop table if exists ers_reimbursement_types; 
drop table if exists ers_reimbursement_statuses; 


create table ers_reimbursement_statuses( 
	reimb_status_id 		serial, 
	reimb_status 			varchar(10) unique, 
	constraint reimb_status_pk 
		primary key (reimb_status_id ) 
);

create table ers_reimbursement_types( 
	reimb_type_id 		serial, 
	reimb_type 			varchar(10) unique, 
	constraint reimb_type_pk 
		primary key (reimb_type_id ) 
);

create table ers_user_roles( 
	role_id 			serial, 
	role_name 			varchar(10) unique, 
	constraint user_role_pk 
		primary key (role_id ) 
); 

create table ers_users( 
	ers_user_id				serial, 
	username 				varchar(25) unique not null, 
	password_hash 			varchar(512) not null, -- should be hashed before being stored in the database. 
	password_salt 			varchar(512) not null, -- salt for the password hash
	firstname 				varchar(25), 
	lastname 				varchar(25), 
	email					varchar(256) unique not null, 
	user_role_id			int, 
	constraint user_id_pk 
		primary key (ers_user_id), 
	constraint user_role_fk 
		foreign key (user_role_id) 
		references ers_user_roles (role_id) 
);

create table ers_reimbursements( 
	reimb_id 				serial, 
	amount 					numeric(6, 2), 
	submitted 				timestamp, 
	resolved 				timestamp, 
	description 			text, 
	receipt 				text, -- stores a link to an AWS S3 bucket object
	author_id 				int, 
	resolver_id 			int, 
	reimb_status_id 		int, 
	reimb_type_id 			int, 
	constraint reimb_pk 
		primary key (reimb_id), 
	constraint reimb_author_fk 
		foreign key (author_id) 
		references ers_users (ers_user_id), 
	constraint reimb_resolver_fk 
		foreign key (resolver_id) 
		references ers_users (ers_user_id), 
	constraint reimb_status_fk 
		foreign key (reimb_status_id) 
		references ers_reimbursement_statuses (reimb_status_id), 
	constraint reimb_type_fk 
		foreign key (reimb_type_id) 
		references ers_reimbursement_types (reimb_type_id) 
);

--create or replace function salt_and_hash_password()
--returns trigger 
--as $$ 
--
--	begin 
--		new.password = crypt(new.password, gen_salt('bf'));
--	end
--
--$$ language plpgsql
--
--create trigger salt_and_hash_passwords 
--before insert or update on ers_users 
--for each row 
--execute function salt_and_hash_password(); 

insert into ers_reimbursement_statuses (reimb_status) 
values 
	('APPROVED'),
	('PENDING'),
	('DENIED')
;

insert into ers_user_roles (role_name) 
values 
	('EMPLOYEE'),
	('MANAGER'),
	('ADMIN')
;

insert into ers_reimbursement_types (reimb_type) 
values 
	('LODGING'),
	('TRAVEL'),
	('FOOD'),
	('OTHER')
;

-- +-------------------------------------------------------------+
-- +                    	  TEST DATA
-- +-------------------------------------------------------------+

insert into ers_users (firstname, lastname , username , password_hash , password_salt,  email , user_role_id )
values
	('Alice', 'Anderson', 'aanderson', 'password', 'salt', 'aanderson@revature.net', 3),
	('Benjamin', 'Barker', 'bbarker', 'password', 'salt', 'bbarker@revature.net', 2),
	('Charlie', 'Courtson', 'ccourtson', 'password', 'salt', 'ccourtson@revature.net', 1),
	('Debra', 'Delion', 'ddelion', 'password', 'salt', 'ddelion@revature.net', 1),
	('Edward', 'Eliotson', 'eeliotson', 'password', 'salt', 'eeliotson@revature.net', 1)
;

--	reimb_id 				serial, 
--	amount 					numeric(6, 2), 
--	submitted 				timestamp, 
--	resolved 				timestamp, 
--	description 			text, 
--	reciept 				text, -- stores a link to an AWS S3 bucket object
--	author_id 				int, 
--	resolver_id 			int, 
--	reimb_status_id 		int, 
--	reimb_type_id 			int, 
insert into ers_reimbursements (amount, 
								submitted, 
								description, 
								author_id, 
								resolver_id, 
								reimb_status_id, 
								reimb_type_id) 
values 
	(150.00, timestamp '2018-01-08 14:05:06', 'traveled to/from test location', 1, 1, 2, 2),
	(150.00, timestamp '2018-01-08 14:05:06', 'traveled to/from test location', 2, 1, 2, 2),
	(150.00, timestamp '2018-01-08 14:05:06', 'traveled to/from test location', 3, 2, 2, 2),
	(150.00, timestamp '2018-01-08 14:05:06', 'traveled to/from test location', 4, 2, 2, 2),
	(150.00, timestamp '2018-01-08 14:05:06', 'traveled to/from test location', 5, 2, 2, 2), -- endregion
	(50.00, timestamp '2018-01-08 14:05:06', 'stayed at hotel6', 1, 1, 2, 1),
	(50.00, timestamp '2018-01-08 14:13:06', 'stayed at hotel6', 2, 1, 2, 1),
	(50.00, timestamp '2018-01-08 14:05:06', 'stayed at hotel6', 3, 2, 2, 1),
	(50.00, timestamp '2018-01-08 14:05:06', 'stayed at hotel6', 4, 2, 2, 1),
	(50.00, timestamp '2018-01-08 14:05:06', 'stayed at hotel6', 5, 2, 2, 1), -- endregion
	(12.00, timestamp '2018-01-09 14:05:06', 'ate breakfast', 1, 1, 2, 3),
	(15.00, timestamp '2018-01-09 14:13:06', 'ate breakfast', 2, 1, 2, 3),
	(15.00, timestamp '2018-01-09 14:05:06', 'ate breakfast', 3, 2, 2, 3),
	(10.00, timestamp '2018-01-09 14:05:06', 'ate breakfast', 4, 2, 2, 3),
	(12.00, timestamp '2018-01-09 14:05:06', 'ate breakfast', 5, 2, 2, 3), -- endregion
	(200.00, timestamp '2018-01-09 14:05:06', 'passed exam', 1, 1, 2, 4),
	(200.00, timestamp '2018-01-09 14:13:06', 'passed exam', 2, 1, 2, 4),
	(200.00, timestamp '2018-01-09 14:05:06', 'passed exam', 3, 2, 2, 4),
	(200.00, timestamp '2018-01-09 14:05:06', 'passed exam', 4, 2, 2, 4),
	(200.00, timestamp '2018-01-09 14:05:06', 'passed exam', 5, 2, 2, 4) -- endregion
;


-- +-------------------------------------------------------------+
-- +                    	  TESTING
-- +-------------------------------------------------------------+
select * 
from revabursement.ers_users eu ;

select eu.firstname, eu.lastname , eu.username , eu.password_hash, eu.email, eur.role_id, eur.role_name
from ers_users eu 
join ers_user_roles eur 
on eu.user_role_id = eur.role_id
order by eu.firstname;

select er.reimb_id, 
	er.amount, 
	er.submitted, 
	er.resolved, 
	er.description, 
	er.receipt, 
	eu.username as author, 
	eu2.username as resolver, 
	ers.reimb_status, 
	ert.reimb_type 
from ers_reimbursements er 
join ers_users eu 
on er.author_id = eu.ers_user_id 
join ers_users eu2 
on er.resolver_id = eu2.ers_user_id 
join ers_reimbursement_statuses ers 
on er.reimb_status_id = ers.reimb_status_id 
join ers_reimbursement_types ert 
on er.reimb_type_id = ert.reimb_type_id 
order by er.reimb_id;

select * 
from ers_reimbursements er ;









-- Hi, Tom! 
