alter table ZPE_RPAGOS add PAG_FCINTR           DATE;
alter table ZPE_RPAGOS add PAG_IDPROC           VARCHAR2(100);

comment on column ZPE_RPAGOS.PAG_FCINTR is
'Fecha inicio tramite';

comment on column ZPE_RPAGOS.PAG_IDPROC is
'Id procedimiento';


alter table ZPE_RPGTPV add TPV_FCINTR           DATE;
alter table ZPE_RPGTPV add TPV_IDPROC           VARCHAR2(100);

comment on column ZPE_RPGTPV.TPV_FCINTR is
'Fecha inicio tramite';

comment on column ZPE_RPGTPV.TPV_IDPROC is
'Id procedimiento';
