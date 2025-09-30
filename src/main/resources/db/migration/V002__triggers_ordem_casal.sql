CREATE OR REPLACE FUNCTION gerenciar_ordem_casal()
RETURNS TRIGGER AS $$
DECLARE
    max_ordem BIGINT;
BEGIN
    IF (TG_OP = 'INSERT') THEN
        IF NEW.cargo = 'casal' THEN
            SELECT COALESCE(MAX(ordem), 0) INTO max_ordem FROM Ordem_Casal;
            INSERT INTO Ordem_Casal (id_casal, ordem) VALUES (NEW.id, max_ordem + 1);
        END IF;

    ELSIF (TG_OP = 'UPDATE') THEN
        IF (NEW.cargo != 'casal' OR NEW.inativo) AND OLD.cargo = 'casal' THEN
            DELETE FROM Ordem_Casal WHERE id_casal = NEW.id;

        ELSIF (NEW.cargo = 'casal' AND NOT NEW.inativo) AND (OLD.cargo != 'casal' OR OLD.inativo) THEN
            DELETE FROM Ordem_Casal WHERE id_casal = NEW.id;

            SELECT COALESCE(MAX(ordem), 0) INTO max_ordem FROM Ordem_Casal;
            INSERT INTO Ordem_Casal (id_casal, ordem) VALUES (NEW.id, max_ordem + 1);
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_inserir_casal_ordem ON Usuario;
DROP TRIGGER IF EXISTS trg_remover_inativos_ordem ON Usuario;
DROP TRIGGER IF EXISTS trg_gerenciar_ordem_casal ON Usuario;

CREATE TRIGGER trg_gerenciar_ordem_casal
AFTER INSERT OR UPDATE ON Usuario
FOR EACH ROW
EXECUTE FUNCTION gerenciar_ordem_casal();