alter Table usuario
    add COLUMN senha_alterada boolean NOT NULL DEFAULT true;

alter table usuario
    alter COLUMN senha_alterada drop default;