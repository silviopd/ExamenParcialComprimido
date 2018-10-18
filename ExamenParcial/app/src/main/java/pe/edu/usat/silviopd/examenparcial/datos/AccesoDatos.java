package pe.edu.usat.silviopd.examenparcial.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pe.edu.usat.silviopd.examenparcial.MainActivity;

/**
 * Created by USER on 15/09/2016.
 */
public class AccesoDatos extends SQLiteOpenHelper {

    private static String nombreBD="bdVendedor";
    private static int versionBD=1;

    public static Context aplicacion;


    private static String tablaDistrito="CREATE TABLE distrito(codigo_distrito int PRIMARY KEY,nombre varchar(100));";

    private static String tablaDistritoDatos[]={
            "insert into distrito values(1,'Chiclayo')",
            "insert into distrito values(2,'La victoria')"
    };

    private static String tablaZona="CREATE TABLE zona(codigo_zona int PRIMARY KEY,codigo_distrito int references distrito(codigo_distrito),nombre varchar(100));";

    private static String tablaZonaDatos[]={
            "insert into zona values(1,1,'Zona 1 Chiclayo')",
            "insert into zona values(2,1,'Zona 2 Chiclayo')",

            "insert into zona values(3,2,'Zona 1 La Victoria')",
            "insert into zona values(4,2,'Zona 2 La Victoria')"
    };

    private static String tablaLineaProducto="CREATE TABLE linea_producto(codigo_linea int NULL PRIMARY KEY,nombre varchar(100));";

    private static String tablaLineaProductoDatos[]={
            "insert into linea_producto values(1,'Bebidas')",
            "insert into linea_producto values(2,'Lacteos')"
    };

    private static String tablaVendedor="CREATE TABLE vendedor(dni char(8) PRIMARY KEY,apellidos varchar(40),nombres varchar(40),direccion varchar(100),telefono varchar(30),tipo_cliente_atiende char (1),latitud real,longitud real,codigo_zona int references zona(codigo_zona),codigo_linea int references linea_producto(codigo_linea),ruta varchar(200));";

    private static String tablaVendedorDatos[]={
            "insert into vendedor values(46705014,'Peña Diaz','Silvio','Las viñas 149 - San isidro',2398234897,'M',-6.7603427,-79.857269,1,1,'')",
            "insert into vendedor values(23947834,'Diaz Jimenez','Alberto','Bolognesi 387',5345647,'N',-6.754498,-79.8373796,2,2,'')"
    };


    public AccesoDatos(){
        super(aplicacion,nombreBD,null,versionBD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    /*Se ejecuta cuando la aplicación es intalada y cargada por primera vez (1 sola vez)*/
        db.execSQL(tablaDistrito);
        db.execSQL(tablaZona);
        db.execSQL(tablaLineaProducto);
        db.execSQL(tablaVendedor);

        for (int i = 0; i < tablaDistritoDatos.length; i++) {
            db.execSQL(tablaDistritoDatos[i]);
        }

        for (int i = 0; i < tablaZonaDatos.length; i++) {
            db.execSQL(tablaZonaDatos[i]);
        }

        for (int i = 0; i < tablaLineaProductoDatos.length; i++) {
            db.execSQL(tablaLineaProductoDatos[i]);
        }

        for (int i = 0; i < tablaVendedorDatos.length; i++) {
            db.execSQL(tablaVendedorDatos[i]);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
