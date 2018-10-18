package pe.edu.usat.silviopd.examenparcial;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pe.edu.usat.silviopd.examenparcial.negocio.Vendedor;

public class VendedorAdaptador extends BaseAdapter {

    public static ArrayList<Vendedor> lista;
    private LayoutInflater layoutInflater;

    public VendedorAdaptador(Context context, ArrayList<Vendedor> lista) {
        this.layoutInflater = LayoutInflater.from(context);
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {  //solo se ve lo q se va scrolleando
            convertView = layoutInflater.inflate(R.layout.vendedor_item, null);
            holder = new Holder();
            holder.lblNombre = (TextView) convertView.findViewById(R.id.lblNombre);
            holder.lblTelefono = (TextView) convertView.findViewById(R.id.lblTelefono);
            holder.lblDireccion = (TextView) convertView.findViewById(R.id.lblDireccion);
            holder.imgImagen = (ImageView) convertView.findViewById(R.id.imgImagen);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();      //solo lo lee sin recargar
        }

        Vendedor item = Vendedor.listaCli.get(position);
        holder.lblNombre.setText(item.getNombres()+" "+item.getApellidos());
        holder.lblDireccion.setText(item.getDireccion());
        holder.lblTelefono.setText(item.getTelefono());

        if (item.getRuta().isEmpty()){
                holder.imgImagen.setImageResource(R.drawable.foto);
            }else{
                holder.imgImagen.setImageURI(Uri.parse(item.getRuta()));
            }

        return convertView;
    }

    private class Holder{   //para llamar a los controles
        TextView lblNombre;
        TextView lblDireccion;
        TextView lblTelefono;
        ImageView imgImagen;
    }


}
