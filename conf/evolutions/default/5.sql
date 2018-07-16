
# --- !Ups
create table configuration (
  account_email             varchar(255)    not null,
  constraint pk_configuration primary key (account_email),
  foreign key (account_email) references account (email) on delete cascade on update restrict
);

# --- !Downs
drop table if exists configuration;