package pe.edu.usat.silviopd.examenparcial;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import pe.edu.usat.silviopd.examenparcial.datos.AccesoDatos;
import pe.edu.usat.silviopd.examenparcial.negocio.Distrito;
import pe.edu.usat.silviopd.examenparcial.negocio.LineaProducto;
import pe.edu.usat.silviopd.examenparcial.negocio.Vendedor;
import pe.edu.usat.silviopd.examenparcial.negocio.Zona;
import pe.edu.usat.silviopd.examenparcial.util.Funciones;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.media.CamcorderProfile.get;
import static pe.edu.usat.silviopd.examenparcial.negocio.Distrito.listaDis;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener{

    EditText txtDni,txtApellidos,txtNombres,txtDireccion,txtTelefono;
    RadioButton rbMayorista,rbMinorista,rbAmbos;
    Spinner spDistrito,spZona,spLineaProducto;
    Button btnGrabar, btnUbicacion,btnFoto;
    ImageView imgFoto;

    boolean userSelect;


    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    private String imgPath = "";

    private LinearLayout mRlView;


    private double latitud = 0, longitud = 0;
    private int position;
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDni = (EditText) findViewById(R.id.txtDni);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtNombres = (EditText) findViewById(R.id.txtNombres);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);

        rbMayorista = (RadioButton) findViewById(R.id.rbMayorista);
        rbMinorista = (RadioButton) findViewById(R.id.rbMinorista);
        rbAmbos = (RadioButton) findViewById(R.id.rbAmbos);

        spDistrito = (Spinner)findViewById(R.id.spDistrito);
        spZona = (Spinner)findViewById(R.id.spZona);
        spLineaProducto = (Spinner)findViewById(R.id.spLineaProducto);

        btnGrabar = (Button) findViewById(R.id.btnGrabar);
        btnUbicacion = (Button) findViewById(R.id.btnUbicacion);
        btnFoto = (Button) findViewById(R.id.btnFoto);

        imgFoto = (ImageView) findViewById(R.id.imgFoto);

        AccesoDatos.aplicacion = this;

        spDistrito.setOnItemSelectedListener(this);
        spZona.setOnItemSelectedListener(this);
        spLineaProducto.setOnItemSelectedListener(this);

        spDistrito.setOnTouchListener(this);

        btnGrabar.setOnClickListener(this);
        btnUbicacion.setOnClickListener(this);
        btnFoto.setOnClickListener(this);

        mRlView = (LinearLayout) findViewById(R.id.rl_view);

        cargarDatosSpinnerDistrito();
        cargarDatosSpinnerLineaProducto();

        Bundle p = this.getIntent().getExtras();
        if (p != null) {//Esta llegando un parametro, significa que debo leer los datos
            this.userSelect = false;

            this.position = p.getInt("position");
            Vendedor item = Vendedor.listaCli.get(position);
            this.leerDatos(item.getDni());

            if (!imgPath.isEmpty()) {
                Uri path = Uri.parse(imgPath);
                imgFoto.setImageURI(path);
            } else {
                imgFoto.setImageResource(R.drawable.foto);
            }
        }else{
            this.userSelect = true;
        }


        if (myRequestStoragePermission()) {
            btnFoto.setEnabled(true);
        } else {
            btnFoto.setEnabled(false);
        }

    }

    private boolean myRequestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicacion", Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void leerDatos(String dni) {
        Cursor cursor = new Vendedor().leerDatos(dni);
        if (cursor.moveToNext()) {
            txtDni.setText(cursor.getString(0));
            txtApellidos.setText(cursor.getString(1));
            txtNombres.setText(cursor.getString(2));
            txtTelefono.setText(cursor.getString(4));
            txtDireccion.setText(cursor.getString(3));

            if (cursor.getString(5).equals("M")){
                rbMayorista.setChecked(true);
            }else if (cursor.getString(5).equals("N")){
                rbMinorista.setChecked(true);
            }else{
                rbAmbos.setChecked(true);
            }

            this.cargarDatosSpinnerZona(cursor.getInt(13));

            Funciones.selectedItemSpinner(spDistrito, cursor.getString(12));
            Funciones.selectedItemSpinner(spZona, cursor.getString(11));
            Funciones.selectedItemSpinner(spLineaProducto, cursor.getString(10));

            this.latitud = cursor.getDouble(6);
            this.longitud = cursor.getDouble(7);
            this.imgPath = cursor.getString(14);

            txtDni.setEnabled(false);
        }
    }

    private void cargarDatosSpinnerDistrito() {
        String listaDepartamento[] = new Distrito().listaDespartamento();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaDepartamento);

        spDistrito.setAdapter(adapter);
    }

    private void cargarDatosSpinnerZona(int codigoDepartamento) {
        String listaProvincia[] = new Zona().listaZona(codigoDepartamento);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaProvincia);

        spZona.setAdapter(adapter);
    }

    private void cargarDatosSpinnerLineaProducto() {
        String listaDepartamento[] = new LineaProducto().listaLineaProducto();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaDepartamento);

        spLineaProducto.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGrabar:

                String dni = txtDni.getText().toString();
                String apellidos = txtApellidos.getText().toString();
                String nombre = txtNombres.getText().toString();
                String direccion = txtDireccion.getText().toString();
                String telefono = txtTelefono.getText().toString();

                String tipo_cliente_atiende;

                if (rbMayorista.isChecked()){
                    tipo_cliente_atiende = "M";
                }else if (rbMinorista.isChecked()){
                    tipo_cliente_atiende = "N";
                }else{
                tipo_cliente_atiende = "A";
                }

                int codigoZona = Zona.listaZon.get(spZona.getSelectedItemPosition()).getCodigo_zona();

                int codigoLinea = LineaProducto.listaLineaProducto.get(spLineaProducto.getSelectedItemPosition()).getCodigo_linea();

                if (dni.isEmpty()) {
                    Toast.makeText(this, "Ingrese dni", Toast.LENGTH_LONG).show();
                    txtDni.requestFocus();
                    return;
                }

                if (apellidos.isEmpty()) {
                    Toast.makeText(this, "Ingrese apellidos", Toast.LENGTH_LONG).show();
                    txtApellidos.requestFocus();
                    return;
                }

                if (nombre.isEmpty()) {
                    Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_LONG).show();
                    txtNombres.requestFocus();
                    return;
                }

                if (direccion.isEmpty()) {
                    Toast.makeText(this, "Ingrese direccion", Toast.LENGTH_LONG).show();
                    txtDireccion.requestFocus();
                    return;
                }

                if (telefono.isEmpty()) {
                    Toast.makeText(this, "Ingrese telefono", Toast.LENGTH_LONG).show();
                    txtTelefono.requestFocus();
                    return;
                }

                if (this.latitud == 0 || this.longitud == 0) {
                    Toast.makeText(this, "Seleccione su posicion", Toast.LENGTH_LONG).show();
                    btnUbicacion.callOnClick();
                    return;
                }

                Vendedor obj = new Vendedor();
                obj.setDni(dni);
                obj.setApellidos(apellidos);
                obj.setNombres(nombre);
                obj.setTelefono(telefono);
                obj.setDireccion(direccion);
                obj.setTipo_cliente_atiende(tipo_cliente_atiende);
                obj.setCodigo_zona(codigoZona);
                obj.setCodigo_linea(codigoLinea);

                obj.setLatitud(this.latitud);
                obj.setLongitud(this.longitud);


                if (!imgPath.isEmpty()) {
                    obj.setRuta(this.imgPath);
                }else {
                    obj.setRuta("");
                }

                long resultado = -1;
                if (txtDni.isEnabled()) {
                    resultado = obj.agregar();
                } else {
                    resultado = obj.editar();
                }

                System.out.println("Resultado: " + resultado);

                if (resultado != -1) {
                    Toast.makeText(this, "Grabado Ok!", Toast.LENGTH_LONG).show();
                    this.finish();
                }

                break;

            case R.id.btnUbicacion:
                Intent m = new Intent(this, VendedorMapaYMapaAsignar.class);
                Bundle pm = new Bundle();
                if (this.txtDni.isEnabled()){
                    pm.putInt("position", -1); //cliente nuevo
                }else{
                    pm.putInt("position", position);
                }

                m.putExtras(pm);
                startActivityForResult(m, REQUEST_CODE);
                break;

            case R.id.btnFoto:

                showOpciones();

                break;
        }
    }

    private void showOpciones() {
        final CharSequence[] option = {"Tomar Foto", "Elegir imagen de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Elija una opcion");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar Foto") {
                    openCamara();
                } else if (option[which] == "Elegir imagen de galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamara() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated) {
            isDirectoryCreated = file.mkdirs();
        }

        if (isDirectoryCreated) {
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            imgPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + imageName;

            File newFile = new File(imgPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));

            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (this.userSelect) {

            this.userSelect = false;

            switch (parent.getId()) {
                case R.id.spDistrito:
                    int dep = listaDis.get(position).getCodigo_distrito();
                    cargarDatosSpinnerZona(dep);
                    this.userSelect=true;
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:

                    MediaScannerConnection.scanFile(this, new String[]{imgPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned path -> " + path + " :");
                            Log.i("ExternalStorage", "-> Uri =" + uri);
                        }
                    });

                    try {
                        ExifInterface exif = new ExifInterface(imgPath);
                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = exifToDegrees(rotation);
                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(rotationInDegrees);
                        }
                        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        imgFoto.setImageBitmap(bitmap);
                        imgFoto.setImageBitmap(rotatedBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    imgPath = String.valueOf(path);
                    imgFoto.setImageURI(path);
                    break;
                case REQUEST_CODE:
                    Bundle p = data.getExtras();
                    this.latitud = p.getDouble("latitud");
                    this.longitud = p.getDouble("longitud");
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos Aceptados", Toast.LENGTH_LONG).show();
                btnFoto.setEnabled(true);
            }
        } else {
            showExPlanation();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("file_path", imgPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imgPath = savedInstanceState.getString("file_path");
    }

    private void showExPlanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permisos Denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.userSelect = true;
        return false;
    }
}
