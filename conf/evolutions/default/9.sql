
# --- !Ups
create table admin (
  email             varchar(255)    not null,
  constraint pk_admin primary key (email),
  foreign key (email) references account (email) on delete cascade on update restrict
);

# --- !Downs
drop table if exists admin;