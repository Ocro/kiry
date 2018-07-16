
# --- !Ups

alter table Game add account_email varchar(255);
alter table Game add constraint fk_game_account foreign key (account_email) references account (email);

# --- !Downs

alter table Game drop column account_email;
alter table Game drop constraint fk_game_account;