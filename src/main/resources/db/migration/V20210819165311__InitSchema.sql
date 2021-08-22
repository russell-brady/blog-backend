create table users(
    id serial primary key,
    username varchar unique not null,
    email_address varchar not null,
    password varchar not null
);

create table posts(
    id serial primary key,
    title varchar not null,
    content varchar not null,
    created_on timestamp not null,
    updated_on timestamp,
    author_id int not null references users(id)
);
