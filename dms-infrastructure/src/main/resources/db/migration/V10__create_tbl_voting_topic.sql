create table tbl_voting_topic
(
    id          binary(16) not null primary key,
    manager_id  binary(16)   not null,
    start_time  datetime(6)  not null,
    end_time    datetime(6)  not null,
    topic_name  varchar(255) not null,
    vote_type   varchar(20)  not null,
    description varchar(255) not null,
    foreign key (manager_id) references tbl_manager (user_id)
);