create table companies (
   id uuid not null,
   active boolean not null,
   name varchar(255) not null,
   tenant_id uuid not null,
   primary key (id)
)