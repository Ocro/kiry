
# --- !Ups

alter table userwishgame add archived boolean not null default false;

# --- !Downs

alter table userwishgame drop column archived;