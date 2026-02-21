CREATE TABLE IF NOT EXISTS public.tenants (
    id uuid NOT NULL,
    code varchar(255) NOT NULL UNIQUE,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);