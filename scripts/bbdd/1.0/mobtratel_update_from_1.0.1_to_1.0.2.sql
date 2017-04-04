create table MOB_CAIBVE  (
   MCE_IDENV            NUMBER(10)                      not null,
   MCE_ESTADO           VARCHAR2(1)                     not null,
   MCE_DESCERR          VARCHAR2(4000)
);

comment on table MOB_CAIBVE is
'Verificacion envios  email de la CAIB
( Tabla propia del plugin de email de la CAIB)';

comment on column MOB_CAIBVE.MCE_IDENV is
'Id envio email';

comment on column MOB_CAIBVE.MCE_ESTADO is
'Estado';

comment on column MOB_CAIBVE.MCE_DESCERR is
'Descripcion error';

alter table MOB_CAIBVE
   add constraint PK_MOB_CAIBVE primary key (MCE_IDENV);
