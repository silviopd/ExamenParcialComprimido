CREATE TABLE distrito
(
  codigo_distrito int PRIMARY KEY,
  nombre varchar(100)
);


CREATE TABLE zona
(
  codigo_zona int PRIMARY KEY,
  codigo_distrito int references distrito(codigo_distrito),
  nombre varchar(100)
);


CREATE TABLE linea_producto
(
  codigo_linea int NULL PRIMARY KEY,
  nombre varchar(100)
);

CREATE TABLE vendedor
(
  dni char(8) PRIMARY KEY,
  apellidos varchar(40),
  nombres varchar(40),
  direccion varchar(100),
  telefono varchar(30),
  tipo_cliente_atiende char (1),
  latitud real,
  longitud real,
  codigo_zona int references zona(codigo_zona),
  codigo_linea int references linea_producto(codigo_linea)
);


select 	v.dni,
		v.apellidos,
		v.nombres,
		v.direccion,
		v.telefono,
		v.tipo_cliente_atiende,
		v.latitud,
		v.longitud,
		v.codigo_zona,
		v.codigo_linea,
		l.nombre,
		z.nombre,
		d.nombre
from vendedor v inner join linea_producto l on v.codigo_linea=l.codigo_linea 
	 inner join zona z on v.codigo_zona=z.codigo_zona and v.codigo_distrito=z.codigo_distrito
	 inner join distrito d on inner z.codigo_distrito=d.codigo_distrito where v.dni = 
