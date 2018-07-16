
# --- !Ups

alter table userwishgame add wish_from timestamp;

# --- !Downs

alter table userwishgame drop column wish_from;