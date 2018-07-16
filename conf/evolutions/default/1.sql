
# --- !Ups

create table account (
  email                     varchar(255)    not null,
  name                      varchar(255)    not null,
  password                  varchar(255)    not null,
  authkey                   varchar(255),
  datesubscription          timestamp       not null,
  constraint pk_account primary key (email)
);

create table platform (
  id                        bigint          not null,
  idgamedb                  bigint,
  name                      varchar(255)    not null,
  constraint pk_platform primary key (id),
  unique(idgamedb)
);

create table compagny (
  id                        bigint          not null,
  name                      varchar(255)    not null,
  constraint pk_compagny primary key (id)
);

create table game (
  id                        bigint          not null,
  idgamedb                  bigint,
  publisher_id              varchar(255),
  developer_id              varchar(255),
  platform_id               bigint          not null,
  gametitle                 varchar(255)    not null,
  youtube                   varchar(2083),
  rating                    smallint,
  releasedate               date,
  constraint pk_game primary key (id),
  foreign key (platform_id) references platform (id) on delete restrict on update restrict,
  foreign key (developer_id) references compagny (id) on delete restrict on update restrict,
  foreign key (publisher_id) references compagny (id) on delete restrict on update restrict
);

create table userwishgame (
  account_email             varchar(255)    not null,
  game_id                   bigint          not null,
  priority                  smallint        not null,
  dateadded                 date            not null,
  constraint pk_userwishgame primary key (account_email, game_id),
  foreign key (account_email) references account (email) on delete cascade on update restrict,
  foreign key (game_id) references game (id) on delete cascade on update restrict
);

create table usergamenotinterrested (
  account_email             varchar(255)    not null,
  game_id                   bigint          not null,
  dateadded                 date            not null,
  constraint pk_usergamenotinterrested primary key (account_email, game_id),
  foreign key (account_email) references account (email) on delete cascade on update restrict,
  foreign key (game_id) references game (id) on delete cascade on update restrict
);

create table notwished (
  account_email                varchar(255)    not null,
  game_id                      bigint          not null,
  reason                       varchar(255),
  dateadded                    date            not null,
  constraint pk_notwished primary key (account_email, game_id),
  foreign key (account_email) references account (email) on delete cascade on update restrict,
  foreign key (game_id) references game (id) on delete cascade on update restrict
);

create table userowngame (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  dateadded                 date            not null,
  constraint pk_userowngame primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);

create table genre (
  id                        bigint          not null,
  name                      varchar(255)    not null,
  constraint pk_genre primary key (id)
);

create table game_genre (
  game_id                   bigint          not null,
  genre_id                  bigint          not null,
  constraint pk_game_genre primary key (game_id, genre_id),
  foreign key (game_id) references game (id) on delete cascade on update restrict,
  foreign key (genre_id) references genre (id) on delete cascade on update restrict
);

create table nextgametoplay (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  ranking                   smallint        not null,
  dateadded                 date            not null,
  constraint pk_nextgametoplay primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);

create table currentplayinggame (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  datebegin                 date            not null,
  constraint pk_currentplayinggame primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);

create table storyfinish (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  datefinish                date            not null,
  timeplayed                bigint,
  constraint pk_storyfinish primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);

create table allcomplete (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  datecomplete              date            not null,
  timeplayed                bigint,
  constraint pk_allcomplete primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);

create table crapgame (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  dateadded                 date            not null,
  constraint pk_crapgame primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);

create sequence game_seq start with 1000;
create sequence genre_seq start with 1000;
create sequence compagny_seq start with 1000;
create sequence platform_seq start with 1000;

# --- !Downs

drop table if exists account;
drop table if exists notwished;
drop table if exists userwishgame;
drop table if exists userowngame;
drop table if exists game;
drop table if exists game_genre;
drop table if exists genre;
drop table if exists platform;
drop table if exists compagny;
drop table if exists nextgametoplay;
drop table if exists currentplayinggame;
drop table if exists storyfinish;
drop table if exists allcomplete;
drop table if exists crapgame;

drop sequence if exists game_seq;
drop sequence if exists genre_seq;
drop sequence if exists compagny_seq;
drop sequence if exists platform_seq