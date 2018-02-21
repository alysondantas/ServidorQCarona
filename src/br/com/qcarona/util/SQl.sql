drop schema public cascade;
create schema public;

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
  qualificacao REAL DEFAULT 2.5 NOT NULL,
  CONSTRAINT usuarios_email_key UNIQUE(email),
  CONSTRAINT usuarios_numero_key UNIQUE(numero),
  CONSTRAINT usuarios_pkey PRIMARY KEY(id)
) 
WITH (oids = false);

CREATE TABLE public.amigos (
    idamizade SERIAL,
    idPrimUsuario INTEGER NOT NULL,
    idSecUsuario INTEGER NOT NULL,
    CONSTRAINT PKAmigos PRIMARY KEY (idamizade),
    CONSTRAINT FKAmigosUsuario FOREIGN KEY(idPrimUsuario) REFERENCES usuarios(id),
    CONSTRAINT FKAmigosUsuario2 FOREIGN KEY(idSecUsuario) REFERENCES usuarios(id)
);

CREATE TABLE public.solicitacaoAmizade (
    idsolicitacao SERIAL,
    idUsuarioSolicitante INTEGER NOT NULL,
    idUsuarioSolicitado INTEGER NOT NULL,
    dataSolicitacao TIMESTAMP,
    CONSTRAINT PKSolicitacao PRIMARY KEY(idsolicitacao),
    CONSTRAINT FKSolicitanteUsuario FOREIGN KEY (idUsuarioSolicitante) REFERENCES usuarios(id),
    CONSTRAINT FKSolicitadoUSuario FOREIGN KEY (idUsuarioSolicitado) REFERENCES usuarios(id)
);

CREATE TABLE public.caronasOfertadas (
    idCaronasOfertadas SERIAL,
    idUsuarioOfertante INTEGER NOT NULL,
    idCidadeOrigem INTEGER NOT NULL,
    idCidadeDestino INTEGER NOT NULL,
    dataHoraSaida TIMESTAMP,
    quantVagas INTEGER NOT NULL,
    CONSTRAINT PKCaronas PRIMARY KEY(idCaronasOfertadas),
    CONSTRAINT FKCaronasUsuario FOREIGN KEY(idUsuarioOfertante) REFERENCES usuarios(id),
    CONSTRAINT FKCaronasCidadeOrigem FOREIGN KEY(idCidadeOrigem) REFERENCES cidade(id_cidade),
    CONSTRAINT FKCaronasCidadeDestino FOREIGN KEY(idCidadeDestino) REFERENCES cidade(id_cidade)
);

CREATE TABLE public.caronasEmAndamento (
    idCaronaAndamento SERIAL,
    idOfertante INTEGER NOT NULL,
    idAproveitador INTEGER NOT NULL,
    idCaronasOfertadas INTEGER NOT NULL,
    CONSTRAINT PKCaronasAndamento PRIMARY KEY(idCaronaAndamento),
    CONSTRAINT FKCaronasUsuario FOREIGN KEY(idOfertante) REFERENCES usuarios(id),
    CONSTRAINT FKCaronasAproveitador FOREIGN KEY(idAproveitador) REFERENCES usuarios(id),
    CONSTRAINT FKCaronas FOREIGN KEY(idCaronasOfertadas) REFERENCES caronasOfertadas(idCaronasOfertadas)
);