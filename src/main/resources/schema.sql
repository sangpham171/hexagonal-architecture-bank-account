-- SCHEMA: bank_account

DROP SCHEMA IF EXISTS bank_account CASCADE;

CREATE SCHEMA IF NOT EXISTS bank_account
    AUTHORIZATION postgres;

-- SEQUENCE: bank_account.bank_account_id_seq

DROP SEQUENCE IF EXISTS bank_account.bank_account_id_seq;

CREATE SEQUENCE IF NOT EXISTS bank_account.bank_account_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE bank_account.bank_account_id_seq
    OWNER TO postgres;

-- Table: bank_account.bank_account

DROP TABLE IF EXISTS bank_account.bank_account;

CREATE TABLE IF NOT EXISTS bank_account.bank_account
(
    id bigint NOT NULL DEFAULT nextval('bank_account.bank_account_id_seq'::regclass),
    available_balance double precision,
    current_withdraw_per_week integer,
    customer_id bigint NOT NULL,
    pending_balance double precision,
    withdraw_limit_per_week integer,
    CONSTRAINT bank_account_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS bank_account.bank_account
    OWNER to admin;

-- SEQUENCE: bank_account.transaction_id_seq

DROP SEQUENCE IF EXISTS bank_account.transaction_id_seq;

CREATE SEQUENCE IF NOT EXISTS bank_account.transaction_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE bank_account.transaction_id_seq
    OWNER TO postgres;
	
-- Table: bank_account.transaction

DROP TABLE IF EXISTS bank_account.transaction;

CREATE TABLE IF NOT EXISTS bank_account.transaction
(
    id bigint NOT NULL DEFAULT nextval('bank_account.transaction_id_seq'::regclass),
    amount double precision NOT NULL,
    currency character varying(3) COLLATE pg_catalog."default" NOT NULL,
    created_date timestamp without time zone NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    transaction_status character varying(20) COLLATE pg_catalog."default" NOT NULL,
    transaction_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    bank_account_id bigint,
    CONSTRAINT transaction_pkey PRIMARY KEY (id),
    CONSTRAINT fkolka13i979w8vrw8mf94xmqdb FOREIGN KEY (bank_account_id)
        REFERENCES bank_account.bank_account (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS bank_account.transaction
    OWNER to admin;