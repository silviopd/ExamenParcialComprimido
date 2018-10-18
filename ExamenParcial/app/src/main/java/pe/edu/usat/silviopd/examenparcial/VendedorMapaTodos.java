package pe.edu.usat.silviopd.examenparcial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import pe.edu.usat.silviopd.examenparcial.negocio.Vendedor;

public class VendedorMapaTodos extends AppCompatActivity implements OnMapReadyCallback{

    private double latitud=0, longitud=0;
    GoogleMap mapa;
    Marker marcador;

    private String nombre,direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Remover title bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_vendedor_mapa_todos);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_asignar);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;

        int cantidad = Vendedor.listaCli.size();

        for (int i = 0; i < cantidad; i++) {
            Vendedor item = Vendedor.listaCli.get(i);
            this.latitud = item.getLatitud();
            this.longitud = item.getLongitud();
            this.nombre = item.getNombres()+" "+item.getApellidos();
            this.direccion = item.getDireccion();

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.draggable(true);
            markerOptions.position(new LatLng(this.latitud, this.longitud));
            markerOptions.title(this.nombre);
            markerOptions.snippet(this.direccion);
            marcador = map.addMarker(markerOptions);

            if (item.getTipo_cliente_atiende().equals("M")){
                marcador.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }else if(item.getTipo_cliente_atiende().equals("N")){
                marcador.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }else{
                marcador.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            }
        }


        marcador.showInfoWindow();

        CameraPosition camPos = new CameraPosition.Builder()
                .target(getCenterCoordinate())
                .zoom(13)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        //map.animateCamera(camUpd3);
        map.moveCamera(camUpd3);
        //map.setTrafficEnabled(true);


    }

    public LatLng getCenterCoordinate(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(this.latitud, this.longitud));
        LatLngBounds bounds = builder.build();
        return bounds.getCenter();
    }

}
