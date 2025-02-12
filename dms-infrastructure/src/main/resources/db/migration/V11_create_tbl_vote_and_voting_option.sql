create table tbl_vote
(
    created_at          datetime(6) not null,
    voted_at            datetime    null,
    id                  binary(16)  not null
        primary key,
    selected_option_id  binary(16)  null,
    selected_student_id binary(16)  null,
    student_id          binary(16)  not null,
    voting_topic_id     binary(16)  not null,
    constraint FKfywyll79shqjgblb9hgx7jq16
        foreign key (voting_topic_id) references tbl_voting_topic (id),
    constraint FKgoy6nfug45mmm8mut8o4v12vp
        foreign key (student_id) references tbl_student (id),
    constraint FKj6gni3626ec0o35qmquc9kuk5
        foreign key (selected_student_id) references tbl_student (id),
    constraint FKtllvyffhwyeousodmuvlbb615
        foreign key (selected_option_id) references tbl_voting_option (id)
);

create table tbl_voting_option
(
    created_at      datetime(6)  not null,
    id              binary(16)   not null
        primary key,
    option_name     varchar(255) null,
    voting_topic_id binary(16)   not null
);