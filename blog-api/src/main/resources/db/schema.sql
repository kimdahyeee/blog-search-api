drop table blog_keyword if exists;

create table blog_keyword
(
    keyword_no      bigint auto_increment   not null comment '키워드 번호',
    keyword         varchar(255)            not null comment '키워드',
    keyword_count   bigint                  not null comment '키워드 조회 횟수',
    create_date     datetime                not null comment '생성 일자',
    update_date     datetime                comment '수정 일자',
    primary key (keyword_no)
);

