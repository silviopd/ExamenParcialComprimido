package pe.edu.usat.silviopd.examenparcial.negocio;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pe.edu.usat.silviopd.examenparcial.datos.AccesoDatos;

/**
 * Created by USER on 13/10/2016.
 */

public class LineaProducto extends AccesoDatos{

    private int codigo_linea;
    private String nombre;

    public static ArrayList<LineaProducto> listaLineaProducto = new ArrayList<LineaProducto>();

    public int getCodigo_linea() {
        return codigo_linea;
    }

    public void setCodigo_linea(int codigo_linea) {
        this.codigo_linea = codigo_linea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private void cargarDatosLineaProducto(){
            SQLiteDatabase bd = this.getReadableDatabase();
            String sql = "select * from linea_producto order by 2";
            Cursor resultado = bd.rawQuery(sql,null);

            listaLineaProducto.clear();

            while(resultado.moveToNext()){
                LineaProducto objDep = new LineaProducto();
                objDep.setCodigo_linea(resultado.getInt(0));
                objDep.setNombre(resultado.getString(1));
                listaLineaProducto.add(objDep);
            }
        }

        public String[] listaLineaProducto(){
            cargarDatosLineaProducto();

            String listaNombresDepartamentos[] = new String[listaLineaProducto.size()];

            for (int i = 0; i < listaLineaProducto.size(); i++) {
                LineaProducto item = listaLineaProducto.get(i);
                listaNombresDepartamentos[i] = item.getNombre();
            }

            return listaNombresDepartamentos;
        }
}
