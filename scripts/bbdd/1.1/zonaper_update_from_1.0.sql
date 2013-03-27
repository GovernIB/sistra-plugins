create sequence ZPE_SEQTPV;

create table ZPE_RPGTPV  (
   TPV_LOCALI           VARCHAR2(100)                   not null,
   TPV_TOKEN            VARCHAR2(100),
   TPV_DATALI           DATE,
   TPV_TIPO             VARCHAR2(1),
   TPV_IDETRA           VARCHAR2(50),
   TPV_MODTRA           VARCHAR2(10),
   TPV_VERTRA           NUMBER(2),
   TPV_NOMTRA           VARCHAR2(200),
   TPV_MODELO           VARCHAR2(100),
   TPV_IDEORG           VARCHAR2(50),
   TPV_IDTASA           VARCHAR2(100),
   TPV_CONCEP           VARCHAR2(200),
   TPV_DATADV           DATE,
   TPV_IMPORT           VARCHAR2(100),
   TPV_NIFDEC           VARCHAR2(10),
   TPV_NOMDEC           VARCHAR2(200),
   TPV_ESTADO           NUMBER(1),
   TPV_TIPOPG           VARCHAR2(1),
   TPV_IDENTP           VARCHAR2(200),
   TPV_DATAPG           DATE,
   TPV_RECIBO           VARCHAR2(4000),
   TPV_URLSTR           VARCHAR2(400),
   TPV_URLMNT           VARCHAR2(400),
   TPV_NOMUSU           VARCHAR2(200),
   TPV_IDIOMA           VARCHAR2(2),
   TPV_FINAL            VARCHAR2(1),
   TPV_HISPED           VARCHAR2(4000)
);

comment on table ZPE_RPGTPV is
'PLUGIN PAGOS TPV';

comment on column ZPE_RPGTPV.TPV_LOCALI is
'Localizador sesion de pago';

comment on column ZPE_RPGTPV.TPV_TOKEN is
'Token sesion pago';

comment on column ZPE_RPGTPV.TPV_DATALI is
'Fecha limite token de inicio sesion pago';

comment on column ZPE_RPGTPV.TPV_TIPO is
'Tipo pago (T/P/A)';

comment on column ZPE_RPGTPV.TPV_IDETRA is
'Identificador del trámite (Id persistencia)';

comment on column ZPE_RPGTPV.TPV_MODTRA is
'Modelo del trámite';

comment on column ZPE_RPGTPV.TPV_VERTRA is
'Versión del trámite';

comment on column ZPE_RPGTPV.TPV_NOMTRA is
'Nombre del trámite';

comment on column ZPE_RPGTPV.TPV_MODELO is
'Modelo tasa';

comment on column ZPE_RPGTPV.TPV_IDEORG is
'Id organismo emisor tasa';

comment on column ZPE_RPGTPV.TPV_IDTASA is
'Identificador tasa';

comment on column ZPE_RPGTPV.TPV_CONCEP is
'Concepto tasa';

comment on column ZPE_RPGTPV.TPV_DATADV is
'Fecha devengo';

comment on column ZPE_RPGTPV.TPV_IMPORT is
'Importe (cents)';

comment on column ZPE_RPGTPV.TPV_NIFDEC is
'Nif declarante';

comment on column ZPE_RPGTPV.TPV_NOMDEC is
'Nombre declarante';

comment on column ZPE_RPGTPV.TPV_ESTADO is
'Estado pago';

comment on column ZPE_RPGTPV.TPV_TIPOPG is
'Tipo pago seleccionado: T / P';

comment on column ZPE_RPGTPV.TPV_IDENTP is
'Identificador pago (numero pedido)';

comment on column ZPE_RPGTPV.TPV_DATAPG is
'Fecha pago';

comment on column ZPE_RPGTPV.TPV_RECIBO is
'Recibo pago en B64.
';

comment on column ZPE_RPGTPV.TPV_URLSTR is
'Url retorno a SISTRA';

comment on column ZPE_RPGTPV.TPV_URLMNT is
'Url mantenimiento sesión SISTRA';

comment on column ZPE_RPGTPV.TPV_NOMUSU is
'Nombre usuario';

comment on column ZPE_RPGTPV.TPV_IDIOMA is
'Idioma';

comment on column ZPE_RPGTPV.TPV_FINAL is
'Indica si el pago ha finalizado';

comment on column ZPE_RPGTPV.TPV_HISPED is
'Historico de pedidos dentro de la sesion de pago separados por coma';

alter table ZPE_RPGTPV
   add constraint ZPE_TPV_PK primary key (TPV_LOCALI);

create unique index ZPE_TPVORD_IDX on ZPE_RPGTPV (
   TPV_IDENTP ASC
);

create table ZPE_TPVNOT  (
   TPN_ORDER            VARCHAR2(15)                    not null,
   TPN_LOCALI           VARCHAR2(100)                   not null,
   TPN_RESULT           VARCHAR2(10)                    not null,
   TPN_SIGNAT           VARCHAR2(500)                   not null,
   TPN_AUTORI           VARCHAR2(10),
   TPN_DATE             VARCHAR2(10),
   TPN_HOUR             VARCHAR2(5)
);

comment on table ZPE_TPVNOT is
'Notificaciones pago TPV';

comment on column ZPE_TPVNOT.TPN_ORDER is
'Orden';

comment on column ZPE_TPVNOT.TPN_LOCALI is
'Localizador sesion pago';

comment on column ZPE_TPVNOT.TPN_RESULT is
'Resultado';

comment on column ZPE_TPVNOT.TPN_SIGNAT is
'Firma respuesta';

comment on column ZPE_TPVNOT.TPN_AUTORI is
'Authorization Code';

comment on column ZPE_TPVNOT.TPN_DATE is
'Fecha (dd/mm/yyyy)';

comment on column ZPE_TPVNOT.TPN_HOUR is
'Hora (HH:mi)';

alter table ZPE_TPVNOT
   add constraint PK_ZPE_TPVNOT primary key (TPN_ORDER);
