alter table ZPE_RPAGOS add PAG_FECINI           DATE;
alter table ZPE_RPAGOS add PAG_FECMAX           DATE;
alter table ZPE_RPAGOS add PAG_MSGMAX           VARCHAR2(4000);

comment on column ZPE_RPAGOS.PAG_FECINI is
'Fecha inicio sesion de pago';

comment on column ZPE_RPAGOS.PAG_FECMAX is
'Fecha limite para realizar el pago (nulo si no hay limite)';

comment on column ZPE_RPAGOS.PAG_MSGMAX is
'Mensaje a mostrar en caso de que se exceda el tiempo de pago';


alter table ZPE_RPGTPV  add TPV_FECINI           DATE;
alter table ZPE_RPGTPV  add TPV_FECMAX           date;
alter table ZPE_RPGTPV  add TPV_MSGMAX           VARCHAR2(4000);

comment on column ZPE_RPGTPV.TPV_FECINI is
'Fecha inicio sesion de pago';

comment on column ZPE_RPGTPV.TPV_FECMAX is
'Fecha limite para realizar el pago (nulo si no hay limite)';

comment on column ZPE_RPGTPV.TPV_MSGMAX is
'Mensaje a mostrar en caso de que se exceda el tiempo de pago';


ALTER TABLE ZPE_RPGTPV MODIFY TPV_MODTRA VARCHAR2(20)
ALTER TABLE ZPE_RPAGOS MODIFY PAG_MODTRA VARCHAR2(20)
