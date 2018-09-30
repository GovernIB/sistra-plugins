alter table ZPE_RPAGOS  add   PAG_NIVAUT           VARCHAR2(1);
alter table ZPE_RPAGOS  add   PAG_NIFUSU           VARCHAR2(50);
alter table ZPE_RPAGOS  add   PAG_CODUSU           VARCHAR2(1536);

comment on column ZPE_RPAGOS.PAG_NIVAUT is
'Nivel de autenticacion de la sesion';

comment on column ZPE_RPAGOS.PAG_NIFUSU is
'Numero de documento del usuario autenticado';

comment on column ZPE_RPAGOS.PAG_CODUSU is
'Codigo de usuario autenticado';

alter table ZPE_RPGTPV	add	  TPV_NIVAUT           VARCHAR2(1);
alter table ZPE_RPGTPV  add   TPV_NIFUSU           VARCHAR2(50);
alter table ZPE_RPGTPV  add   TPV_CODUSU           VARCHAR2(1536);

comment on column ZPE_RPGTPV.TPV_NIVAUT is
'Nivel de autenticacion de la sesion';

comment on column ZPE_RPGTPV.TPV_NIFUSU is
'Numero de documento del usuario autenticado';

comment on column ZPE_RPGTPV.TPV_CODUSU is
'Codigo de usuario autenticado';