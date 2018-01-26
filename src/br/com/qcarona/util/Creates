CREATE TABLE public.usuarios (
  id SERIAL,
  nome VARCHAR(100) NOT NULL,
  sobrenome VARCHAR(100),
  email VARCHAR(100) NOT NULL,
  numero VARCHAR(100) NOT NULL,
  senha VARCHAR(32) NOT NULL,
  CONSTRAINT usuarios_email_key UNIQUE(email),
  CONSTRAINT usuarios_numero_key UNIQUE(numero),
  CONSTRAINT usuarios_pkey PRIMARY KEY(id)
) 
WITH (oids = false);