package pe.edu.usat.silviopd.examenparcial.negocio;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pe.edu.usat.silviopd.examenparcial.datos.AccesoDatos;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by USER on 13/10/2016.
 */

public class Zona extends AccesoDatos {

    private int codigo_zona;
    private int codigo_distrito;
    private String nombre;

    public static ArrayList<Zona> listaZon = new ArrayList<Zona>();

    public int getCodigo_distrito() {
        return codigo_distrito;
    }

    public void setCodigo_distrito(int codigo_distrito) {
        this.codigo_distrito = codigo_distrito;
    }

    public int getCodigo_zona() {
        return codigo_zona;
    }

    public void setCodigo_zona(int codigo_zona) {
        this.codigo_zona = codigo_zona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private void cargarDatosZona(int codigoDistrito){
        SQLiteDatabase bd = this.getReadableDatabase();
        String sql = "select * from zona where codigo_distrito = "+codigoDistrito+"";
        Cursor resultado = bd.rawQuery(sql,null);

        listaZon.clear();

        while(resultado.moveToNext()){
            Zona objPro = new Zona();
            objPro.setCodigo_zona(resultado.getInt(0));
            objPro.setCodigo_distrito(resultado.getInt(1));
            objPro.setNombre(resultado.getString(2));
            listaZon.add(objPro);
        }
    }

    public String[] listaZona(int codigoDistrito){
        cargarDatosZona(codigoDistrito);

        String listaNombresProvincia[] = new String[listaZon.size()];

        for (int i = 0; i < listaZon.size(); i++) {
            Zona item = listaZon.get(i);
            listaNombresProvincia[i] = item.getNombre();
        }

        return listaNombresProvincia;
    }
}
