package pe.edu.usat.silviopd.examenparcial.negocio;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pe.edu.usat.silviopd.examenparcial.datos.AccesoDatos;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by USER on 13/10/2016.
 */

public class Distrito extends AccesoDatos {

    private int codigo_distrito;
    private String nombre;

    public static ArrayList<Distrito> listaDis = new ArrayList<Distrito>();

    public int getCodigo_distrito() {
        return codigo_distrito;
    }

    public void setCodigo_distrito(int codigo_distrito) {
        this.codigo_distrito = codigo_distrito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private void cargarDatosDistrito(){
        SQLiteDatabase bd = this.getReadableDatabase();
        String sql = "select * from distrito order by 2";
        Cursor resultado = bd.rawQuery(sql,null);

        listaDis.clear();

        while(resultado.moveToNext()){
            Distrito objDep = new Distrito();
            objDep.setCodigo_distrito(resultado.getInt(0));
            objDep.setNombre(resultado.getString(1));
            listaDis.add(objDep);
        }
    }

    public String[] listaDespartamento(){
        cargarDatosDistrito();

        String listaNombresDepartamentos[] = new String[listaDis.size()];

        for (int i = 0; i < listaDis.size(); i++) {
            Distrito item = listaDis.get(i);
            listaNombresDepartamentos[i] = item.getNombre();
        }

        return listaNombresDepartamentos;
    }
}
