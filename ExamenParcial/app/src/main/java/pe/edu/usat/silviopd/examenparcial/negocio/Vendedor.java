package pe.edu.usat.silviopd.examenparcial.negocio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pe.edu.usat.silviopd.examenparcial.datos.AccesoDatos;

/**
 * Created by USER on 13/10/2016.
 */

public class Vendedor extends AccesoDatos {

    private String dni;
    private String apellidos;
    private String nombres;
    private String direccion;
    private String telefono;
    private String tipo_cliente_atiende;
    private double latitud;
    private double longitud;
    private int codigo_zona;
    private int codigo_linea;

    private String ruta;

    public static ArrayList<Vendedor> listaCli = new ArrayList<Vendedor>();

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getCodigo_linea() {
        return codigo_linea;
    }

    public void setCodigo_linea(int codigo_linea) {
        this.codigo_linea = codigo_linea;
    }

    public int getCodigo_zona() {
        return codigo_zona;
    }

    public void setCodigo_zona(int codigo_zona) {
        this.codigo_zona = codigo_zona;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo_cliente_atiende() {
        return tipo_cliente_atiende;
    }

    public void setTipo_cliente_atiende(String tipo_cliente_atiende) {
        this.tipo_cliente_atiende = tipo_cliente_atiende;
    }

    public long agregar(){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("dni",this.getDni());
        valores.put("apellidos",this.getApellidos());
        valores.put("nombres",this.getNombres());
        valores.put("direccion",this.getDireccion());
        valores.put("telefono",this.getTelefono());
        valores.put("tipo_cliente_atiende",this.getTipo_cliente_atiende());
        valores.put("latitud",this.getLatitud());
        valores.put("longitud",this.getLongitud());
        valores.put("codigo_zona",this.getCodigo_zona());
        valores.put("codigo_linea",this.getCodigo_linea());
        valores.put("ruta",this.getRuta());

        long resultado = db.insert("vendedor",null,valores);

        return resultado;
    }

    public void cargarLista(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultado = db.rawQuery("select * from vendedor order by nombres", null);
        listaCli.clear();
        while(resultado.moveToNext()){
            Vendedor obj = new Vendedor();
            obj.setDni(resultado.getString(0));
            obj.setApellidos(resultado.getString(1));
            obj.setNombres(resultado.getString(2));
            obj.setDireccion(resultado.getString(3));
            obj.setTelefono(resultado.getString(4));
            obj.setTipo_cliente_atiende(resultado.getString(5));
            obj.setLatitud(resultado.getDouble(6));
            obj.setLongitud(resultado.getDouble(7));
            obj.setCodigo_zona(resultado.getInt(8));
            obj.setCodigo_linea(resultado.getInt(9));
            obj.setRuta(resultado.getString(10));
            listaCli.add(obj);
        }
    }

    public long eliminar(String dni){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("vendedor", "dni = " + dni + "", null);
    }

    public Cursor leerDatos(String dni){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql ="select v.dni,v.apellidos,v.nombres,v.direccion,v.telefono,v.tipo_cliente_atiende,v.latitud,v.longitud,v.codigo_zona,v.codigo_linea,l.nombre, z.nombre, d.nombre,z.codigo_distrito,v.ruta from vendedor v inner join linea_producto l on v.codigo_linea=l.codigo_linea inner join zona z on v.codigo_zona=z.codigo_zona inner join distrito d on z.codigo_distrito=d.codigo_distrito where v.dni =" + dni + "";

        Cursor resultado = db.rawQuery(sql, null);
        return resultado;
    }

    public long editar(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        //valores.put("dni",this.getDni());

        valores.put("apellidos",this.getApellidos());
        valores.put("nombres",this.getNombres());
        valores.put("direccion",this.getDireccion());
        valores.put("telefono",this.getTelefono());
        valores.put("tipo_cliente_atiende",this.getTipo_cliente_atiende());
        valores.put("latitud",this.getLatitud());
        valores.put("longitud",this.getLongitud());
        valores.put("codigo_zona",this.getCodigo_zona());
        valores.put("codigo_linea",this.getCodigo_linea());
        valores.put("ruta",this.getRuta());

        String condicion = "dni = "+this.getDni()+"";

        long resultado = db.update("vendedor",valores,condicion,null);

        return resultado;
    }

    public long editarUbicacion(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("latitud",this.getLatitud());
        valores.put("longitud",this.getLongitud());

        String condicion = "dni = "+this.getDni()+" ";

        long resultado = db.update("vendedor",valores,condicion,null);

        return resultado;
    }
}
