create table card (
    id  serial not null,
    card_holder_name varchar(255),
    card_period int4,
    code_word varchar(255),
    urgent boolean,
    primary key (id)
);

create table card_type (
    id  serial not null,
    name varchar(255),
    primary key (id)
);
create table orders (
    id  serial not null,
    creation_time timestamp,
    status varchar(255),
    primary key (id)
);

create table users (
    id  serial not null,
    password varchar(255),
    role varchar(255),
    username varchar(255),
    primary key (id)
);



create table card (
    id  serial not null,
    account_number varchar(255),
    card_holder_name varchar(255),
    card_number varchar(255),
    card_period int4,
    code_word varchar(255),
    urgent boolean,
    type_id int4, primary key (id)
)

create table card_type (id  serial not null, name varchar(255), primary key (id))
create table orders (id  serial not null, creation_time timestamp, status varchar(255), card_id int4, card_type_id int4, user_id int4, primary key (id))
create table users (id  serial not null, password varchar(255), role varchar(255), username varchar(255), primary key (id))