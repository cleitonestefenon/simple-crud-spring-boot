create table product (
  pro_id uuid not null,
  pro_name varchar(50) not null,
  pro_description text not null,
  pro_price numeric not null,
  constraint pk_product primary key (pro_id)
);
