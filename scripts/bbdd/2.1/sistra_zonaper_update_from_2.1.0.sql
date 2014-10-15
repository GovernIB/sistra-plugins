create table ZPE_REGUSU  (
   URG_TIPREG           VARCHAR2(1)                     not null,
   URG_NUMREG           VARCHAR2(50)                    not null,
   URG_FECREG           DATE                            not null,
   URG_USUREG           VARCHAR2(1536)                  not null
);

comment on table ZPE_REGUSU is
'Tabla especifica CAIB para almacenar los usuarios con los que se realizan los registros';

comment on column ZPE_REGUSU.URG_TIPREG is
'Tipo de registro  Entrada (E) / Salida (S)';

comment on column ZPE_REGUSU.URG_NUMREG is
'Numero de registro';

comment on column ZPE_REGUSU.URG_FECREG is
'Fecha registro';

comment on column ZPE_REGUSU.URG_USUREG is
'Usuario que realiza el registro';

alter table ZPE_REGUSU
   add constraint ZPE_URG_PK primary key (URG_TIPREG, URG_NUMREG);
