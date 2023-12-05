create
    database powerink;

use
    powerink;

create table device
(
    id          varchar(50)   not null
        primary key,
    name        varchar(1024) null,
    description varchar(2048) null,
    owner       bigint        null,
    mode        int default 0 not null,
    status      int default 0 not null,
    lastSeen    datetime      null,
    ip          varchar(1024) null
);

create table user
(
    id       bigint                              not null
        primary key,
    username varchar(256)                        not null,
    password varchar(1024)                       not null,
    nickname varchar(256) default 'PowerInk用户' null,
    regTime  datetime                            null,
    role     varchar(256)                        null,
    isValid  tinyint      default 1              null,
    constraint user_pk2
        unique (username)
);
