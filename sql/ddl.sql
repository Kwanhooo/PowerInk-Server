create
    database powerink;

use
    powerink;

create table powerink.device
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

