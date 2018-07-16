
# --- !Ups
create table userowngame (
  account_email             varchar(255)    not null,
  game_id                   bigint          not null,
  dateadded                 date            not null,
  constraint pk_userowngame primary key (account_email, game_id),
  foreign key (account_email) references account (email) on delete cascade on update restrict,
  foreign key (game_id) references game (id) on delete cascade on update restrict
);

# --- !Downs
drop table if exists userowngame;