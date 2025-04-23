drop table if exists exception_event CASCADE;
drop table if exists request_event CASCADE;
drop table if exists server CASCADE;
drop table if exists sql_event CASCADE;
drop sequence if exists event_seq;
drop sequence if exists server_seq;
create sequence event_seq start with 1 increment by 1;
create sequence server_seq start with 1 increment by 1;
create table exception_event
(
    id                integer   not null,
    analysis_required boolean,
    duration          integer   not null,
    thread_id         varchar(10),
    time              timestamp not null,
    user_id           varchar(30),
    server_id         integer   not null,
    exception_name    varchar(255),
    occurance_class   varchar(255),
    occurance_line    integer,
    occurance_method  varchar(255),
    primary key (id)
);
create table request_event
(
    id                integer      not null,
    analysis_required boolean,
    duration          integer      not null,
    thread_id         varchar(10),
    time              timestamp    not null,
    user_id           varchar(30),
    server_id         integer      not null,
    method            varchar(255) not null,
    primary key (id)
);
create table server
(
    id               integer      not null,
    ip               varchar(255) not null,
    created_date     timestamp    not null default current_timestamp,
    last_update_date timestamp    not null default current_timestamp,
    name             varchar(255) not null,
    version          bigint,
    is_active        boolean      not null default true,
    primary key (id)
);
create table sql_event
(
    id                integer       not null,
    analysis_required boolean,
    duration          integer       not null,
    thread_id         varchar(10),
    time              timestamp     not null,
    user_id           varchar(30),
    server_id         integer       not null,
    sql_query         varchar(4000) not null,
    primary key (id)
);
alter table exception_event
    add constraint FK_exc_ev_server foreign key (server_id) references server;
alter table request_event
    add constraint FK_req_ev_event foreign key (server_id) references server;
alter table sql_event
    add constraint FK__sql_ev_server foreign key (server_id) references server;
create table comment
(
    id              integer         not null,
    content         varchar(255)    not null,
    primary key (id)
);
create table follower
(
    id              integer         not null,
    user_id         varchar(30)     not null,
    comment_id      integer         not null,
    subscription_date timestamp     not null,
    primary key (id)
);
alter table follower
    add constraint FK_foll_comment foreign key (comment_id) references comment;
create table comment_event
(
    comment_id      integer         not null,
    event_id        integer         not null,
    primary key (comment_id, event_id)
);
alter table comment_event
    add constraint FK_comment_event_comment foreign key (comment_id) references comment;