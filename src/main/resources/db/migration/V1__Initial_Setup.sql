create table customer(
    id BIGSERIAL PRIMARY KEY ,
    name text NOT NULL ,
    email  text NOT NULL ,
    password  text NOT NULL ,
    gender  text NOT NULL ,
    age int not null
)