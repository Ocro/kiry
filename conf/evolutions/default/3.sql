
# --- !Ups
drop table if exists crapgame;
drop table if exists allcomplete;
drop table if exists currentplayinggame;
drop table if exists nextgametoplay;
drop table if exists storyfinish;
drop table if exists userowngame;

# --- !Downs
create table crapgame (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  dateadded                 date            not null,
  constraint pk_crapgame primary key (idaccount, idgame),
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

create table currentplayinggame (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  datebegin                 date            not null,
  constraint pk_currentplayinggame primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
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

create table storyfinish (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  datefinish                date            not null,
  timeplayed                bigint,
  constraint pk_storyfinish primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);

create table userowngame (
  idaccount                 varchar(255)    not null,
  idgame                    bigint          not null,
  dateadded                 date            not null,
  constraint pk_userowngame primary key (idaccount, idgame),
  foreign key (idaccount) references account (email) on delete cascade on update restrict,
  foreign key (idgame) references game (id) on delete cascade on update restrict
);