# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table evaluation (
  id                        bigint auto_increment not null,
  q1                        varchar(255),
  q2                        varchar(255),
  q3                        varchar(255),
  constraint pk_evaluation primary key (id))
;

create table user (
  email                     varchar(255) not null,
  name                      varchar(255),
  surname                   varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (email))
;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists evaluation;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_seq;

