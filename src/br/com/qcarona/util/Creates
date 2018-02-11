CREATE TABLE public.usuarios (
  id SERIAL,
  nome VARCHAR(100) NOT NULL,
  sobrenome VARCHAR(100),
  email VARCHAR(100) NOT NULL,
  numero VARCHAR(100) NOT NULL,
  senha VARCHAR(32) NOT NULL,
  cep VARCHAR(9),
  data VARCHAR(10),
  datacadastrado DATE DEFAULT 'now'::text::date,
  CONSTRAINT usuarios_email_key UNIQUE(email),
  CONSTRAINT usuarios_numero_key UNIQUE(numero),
  CONSTRAINT usuarios_pkey PRIMARY KEY(id)
) 
WITH (oids = false);