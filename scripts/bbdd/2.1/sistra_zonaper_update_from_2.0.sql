alter table ZPE_TPVNOT  add TPN_SIGDAT           VARCHAR2(4000);

comment on column ZPE_TPVNOT.TPN_SIGDAT is
'Datos firmados';
