CREATE TABLE Usuario (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo VARCHAR(50) NOT NULL CHECK (cargo IN ('admin', 'secretaria', 'casal', 'coordenador')),
    nome VARCHAR(100),
    marido VARCHAR(100),
    mulher VARCHAR(100),
    inativo BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE Batizado (
    id SERIAL PRIMARY KEY,
    data TIMESTAMP NOT NULL UNIQUE,
    celebrante VARCHAR(100),
    id_casal INTEGER NOT NULL,
    CONSTRAINT fk_casal FOREIGN KEY (id_casal) REFERENCES Usuario(id)
);

CREATE TABLE Catecumeno (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    id_batizado INTEGER NOT NULL,
    CONSTRAINT fk_catecumeno FOREIGN KEY (id_batizado) REFERENCES Batizado(id) ON DELETE CASCADE,
    CONSTRAINT uq_nome_batizado UNIQUE (nome, id_batizado)
);

-- Com _ para compatibilidade
CREATE TABLE Ordem_Casal (
    id_casal INTEGER PRIMARY KEY,
    ordem INTEGER,
    CONSTRAINT fk_casal FOREIGN KEY (id_casal) REFERENCES Usuario(id)
);


-- Triggers

CREATE OR REPLACE FUNCTION inserir_casal_ordem()
RETURNS TRIGGER AS $$
DECLARE
    max_ordem BIGINT;
BEGIN
    -- Pega o maior valor atual de ordem na tabela
    SELECT COALESCE(MAX(ordem), 0) INTO max_ordem FROM Ordem_Casal;

    -- Insere o novo casal com ordem = maior + 1
    INSERT INTO Ordem_Casal (id_casal, ordem)
    VALUES (NEW.id, max_ordem + 1);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_inserir_casal_ordem ON Usuario;
CREATE TRIGGER trg_inserir_casal_ordem
AFTER INSERT ON Usuario
FOR EACH ROW
WHEN (NEW.cargo = 'casal')
EXECUTE FUNCTION inserir_casal_ordem();


create or replace function remover_inativos_ordem() returns trigger as $$
begin
    if new.cargo != 'casal' THEN
        return new;
    end if;

    if new.inativo then
        delete from ordem_casal where id_casal = new.id;
    else
        insert into ordem_casal values (new.id, default) on conflict do nothing;
    end if;

    return new;
end;
$$ language plpgsql;


drop trigger if exists trg_remover_inativos_ordem on usuario;
create trigger trg_remover_inativos_ordem
after update on usuario
for each row
execute function remover_inativos_ordem();
