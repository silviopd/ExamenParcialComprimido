package pe.edu.usat.silviopd.examenparcial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pe.edu.usat.silviopd.examenparcial.datos.AccesoDatos;
import pe.edu.usat.silviopd.examenparcial.negocio.Vendedor;
import pe.edu.usat.silviopd.examenparcial.util.Funciones;

public class VendedorListado extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab,fab2;
    ListView lvLista;

    ArrayList<Vendedor> lista;
    VendedorAdaptador adaptador;

    private static final int REQUEST_CODE = 0;
    private double latitud=0, longitud=0;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_listado);

        AccesoDatos.aplicacion = this;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(this);

        lvLista = (ListView) findViewById(R.id.lvListado);

        registerForContextMenu(lvLista);

        listar();
    }

    private void listar() {
        new Vendedor().cargarLista();
        lista = new ArrayList<Vendedor>();
        lista = Vendedor.listaCli;
        adaptador = new VendedorAdaptador(this, lista);
        lvLista.setAdapter(adaptador);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.fab:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;

            case R.id.fab2:
                Intent n = new Intent(this, VendedorMapaTodos.class);
                startActivity(n);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        listar();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvListado) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(Vendedor.listaCli.get(info.position).getNombres()+" "+Vendedor.listaCli.get(info.position).getApellidos());
            String[] menuItems = getResources().getStringArray(R.array.menu);    //menu del strings.xml
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        switch (menuItemIndex) {
            case 0:  //editar
                Intent i = new Intent(this,MainActivity.class);
                Bundle p = new Bundle();
                p.putInt("position",info.position);
                this.position = info.position;
                i.putExtras(p);
                startActivity(i);

                break;
            case 1:  //eliminar

                boolean r = Funciones.mensajeConfirmacion(this, "Confirme", "Desea eliminar");
                if (r == false) {
                    return false;
                }

                String dni = Vendedor.listaCli.get(info.position).getDni();
                long resultado = new Vendedor().eliminar(dni);
                if (resultado > 0) {
                    Toast.makeText(this, "Registro eliminado", Toast.LENGTH_LONG).show();
                    listar();
                }
                break;
            case 2: //Ver en el mapa
                Intent m = new Intent(this, VendedorMapaYMapaAsignar.class);
                Bundle pm = new Bundle();
                pm.putInt("position", info.position);
                m.putExtras(pm);
                //startActivity(m);
                startActivityForResult(m, REQUEST_CODE);
                break;
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                Bundle p = data.getExtras();
                this.latitud = p.getDouble("latitud");
                this.longitud = p.getDouble("longitud");

                Vendedor objCliente = new Vendedor();
                objCliente.setLatitud(this.latitud);
                objCliente.setLongitud(this.longitud);
                objCliente.setDni(Vendedor.listaCli.get(this.position).getDni());
                objCliente.editarUbicacion();

                Toast.makeText(this, "Actualizando la ubicación del cliente\n\n" + "Lat: " + this.latitud + ", Long: " + this.longitud, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
