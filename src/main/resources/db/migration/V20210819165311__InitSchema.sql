create table users(
    id serial primary key,
    name varchar not null,
    email varchar unique not null,
    password varchar not null
);

create table posts(
    id serial primary key,
    title varchar not null,
    content varchar not null,
    created_on timestamp not null,
    updated_on timestamp not null,
    author_id int not null references users(id)
);
