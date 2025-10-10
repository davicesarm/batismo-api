alter table batizado
    add column if not exists casal_alocado_manualmente boolean default false;

alter table catecumeno
    drop constraint if exists uq_nome_batizado;