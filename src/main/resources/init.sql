CREATE TABLE IF NOT EXISTS public.person
(
    id uuid NOT NULL,
    fullname character varying(40) COLLATE pg_catalog."default" NOT NULL,
    year_of_birth integer NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.author
(
    id uuid NOT NULL,
    fullname character varying(40) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT author_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.book
(
    id uuid NOT NULL,
    person_id uuid,
    title character varying(40) COLLATE pg_catalog."default" NOT NULL,
    year integer NOT NULL,
    CONSTRAINT book_pkey PRIMARY KEY (id),
    CONSTRAINT book_person_id_fkey FOREIGN KEY (person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

