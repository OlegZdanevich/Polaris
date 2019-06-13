create table hibernate_sequences
(
    sequence_name varchar(255) not null
        primary key,
    next_val      bigint       null
);

INSERT INTO polaris.hibernate_sequences (sequence_name, next_val) VALUES ('default', 28);
create table project
(
    id         bigint auto_increment
        primary key,
    name       varchar(140) not null,
    created_at datetime     null,
    updated_at datetime     null,
    created_by bigint       null,
    updated_by bigint       null,
    file_name  varchar(140) null
)
    charset = utf8;

INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (10, 'A-0', '2019-05-15 10:27:44', '2019-05-15 10:27:44', 1, 1, 'A-0.txt');
INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (11, 'A-1', '2019-05-15 10:27:50', '2019-05-15 10:27:50', 1, 1, 'A-1.txt');
INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (12, 'A-2', '2019-05-15 10:27:56', '2019-05-15 10:27:56', 1, 1, 'A-3.txt');
INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (13, 'A-3', '2019-05-15 10:28:01', '2019-05-15 10:28:01', 1, 1, 'A-4.txt');
INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (14, 'A-4', '2019-05-15 10:28:08', '2019-05-15 10:28:08', 1, 1, 'A-5.txt');
INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (15, 'A-5', '2019-05-15 10:28:13', '2019-05-15 10:28:13', 1, 1, 'A-6.txt');
INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (16, 'A-6', '2019-05-15 10:28:19', '2019-05-15 10:28:19', 1, 1, 'A-7.txt');
INSERT INTO polaris.project (id, name, created_at, updated_at, created_by, updated_by, file_name) VALUES (20, 'A-10', '2019-05-15 10:28:42', '2019-05-15 10:28:42', 1, 1, 'A-2.txt');
create table roles
(
    id   bigint auto_increment
        primary key,
    name varchar(60) not null,
    constraint UK_nb4h0p6txrmfc0xbrd1kglp9t
        unique (name),
    constraint uk_roles_name
        unique (name)
)
    charset = utf8;

INSERT INTO polaris.roles (id, name) VALUES (5, 'ROLE_ADMIN');
INSERT INTO polaris.roles (id, name) VALUES (4, 'ROLE_USER');
create table user_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_user_roles_role_id
        foreign key (role_id) references roles (id),
    constraint fk_user_roles_user_id
        foreign key (user_id) references users (id)
)
    charset = utf8;

INSERT INTO polaris.user_roles (user_id, role_id) VALUES (1, 4);
create table users
(
    id         bigint auto_increment
        primary key,
    name       varchar(40)  not null,
    username   varchar(15)  not null,
    email      varchar(40)  not null,
    password   varchar(100) not null,
    created_at datetime     null,
    updated_at datetime     null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UKr43af9ap4edm43mmtq01oddj6
        unique (username),
    constraint uk_users_email
        unique (email),
    constraint uk_users_username
        unique (username)
)
    charset = utf8;

INSERT INTO polaris.users (id, name, username, email, password, created_at, updated_at) VALUES (1, 'Oleg Zdanevich', 'oleg', 'oleg.zdanevich@icloud.com', '$2a$10$c51Dovrd5MsRoE.cGjuwbulZYLr44Cv6V1ggraqJxFin69lsC6Vvi', '2019-01-24 20:10:21', '2019-01-24 20:10:21');