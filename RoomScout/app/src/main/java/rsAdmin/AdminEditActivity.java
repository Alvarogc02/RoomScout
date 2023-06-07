package rsAdmin;

import static rsConexion.Conexion.mongoCollection;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import rsConexion.Conexion;
import rsHoteles.ListActivity;
import rsObjetos.Hotel;

import com.example.roomscout.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import org.bson.Document;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class AdminEditActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText etNombre, etPrecio;
    private TextView tvDireccion;
    private Button btnGuardar, btnVolver;

    private GoogleMap mMap;
    private Marker marcadorActual;

    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private int precio;

    private String hotelAEditar;

    Hotel hotel;

    // Hacer que no se pueda volver a la ventana anterior pulsando el botón del movil
    @Override
    public void onBackPressed() {}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

        Bundle bundle = this.getIntent().getExtras();
        hotelAEditar = bundle.getString("nombreHotel");


        etNombre = findViewById(R.id.editTextNombre);
        etPrecio = findViewById(R.id.editTextPrecio);

        tvDireccion = findViewById(R.id.tvDireccion);

        btnVolver = findViewById(R.id.btnVolver);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Obtener el mapa asincrónicamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Conexion.conectarBD(AdminEditActivity.this);
        new ConexionTask().execute();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEditActivity.this, AdminListActivity.class);
                startActivity(intent);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = etNombre.getText().toString();
                direccion = tvDireccion.getText().toString();

                // Verificar si algún campo está vacío
                if (nombre.isEmpty() || direccion.equals("") || etPrecio.getText().toString().isEmpty()) {
                    Toast.makeText(AdminEditActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    precio = Integer.parseInt(etPrecio.getText().toString());
                    Document updateDocument = new Document().append("$set", new Document()
                            .append("nombre", nombre)
                            .append("direccion", direccion)
                            .append("precio/noche", precio)
                            .append("latitud", latitud)
                            .append("longitud", longitud));

                    mongoCollection.updateOne(new Document("nombre", hotelAEditar), updateDocument).getAsync(updateTask -> {
                        if (updateTask.isSuccess()) {
                            Toast.makeText(AdminEditActivity.this, "Hotel editado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminEditActivity.this, AdminListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AdminEditActivity.this, "Error al editar hotel", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public class ConexionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Conexion.conectarCollection("hoteles");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("ObtenerHotelesTask", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("ObtenerHotelesTask", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find(new Document("nombre", hotelAEditar)).iterator();
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    while (task.get().hasNext()) {
                        Document resultData = task.get().next();
                        List<String> listaFav = resultData.getList("favorito_de", String.class);
                        String[] arrayFav = listaFav.toArray(new String[listaFav.size()]);

                        Hotel hotel = new Hotel(
                                resultData.getString("nombre"),
                                resultData.getString("direccion"),
                                resultData.getInteger("precio/noche"),
                                resultData.getDouble("latitud"),
                                resultData.getDouble("longitud"),
                                resultData.getInteger("valoracion"),
                                arrayFav
                        );
                        Log.d("ObtenerHotelesTask", "Hotel: " + hotel.getNombre());

                        etNombre.setText(hotel.getNombre());
                        tvDireccion.setText(hotel.getDireccion());
                        etPrecio.setText(String.valueOf(hotel.getPrecio()));

                        // Añadir marcador segun hotel.getLatitud() y hotel.getLongitud()
                        LatLng ubicacion = new LatLng(hotel.getLatitud(), hotel.getLongitud());
                        marcadorActual = mMap.addMarker(new MarkerOptions().position(ubicacion));


                    }
                } else {
                    Log.d("ObtenerHotelesTask", "Error al buscar hoteles");
                }
            });
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng centro = new LatLng(40.10, -4.30);
        float zoomLevel = 5.5f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(centro, zoomLevel);
        mMap.moveCamera(cameraUpdate);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitud = formatearDouble(latLng.latitude);
                longitud = formatearDouble(latLng.longitude);


                if (marcadorActual != null) {
                    marcadorActual.remove();
                }

                LatLng marcador = new LatLng(latitud, longitud);
                marcadorActual = mMap.addMarker(new MarkerOptions().position(marcador));

                Geocoder geocoder = new Geocoder(AdminEditActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitud, longitud, 1);
                    if (addresses.size() > 0 && addresses != null) {
                        direccion = addresses.get(0).getAddressLine(0);
                        Toast.makeText(AdminEditActivity.this, latitud + "\n" + longitud, Toast.LENGTH_SHORT).show();
                        tvDireccion.setText(direccion);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public double formatearDouble(double valor) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.#####", symbols);
        return Double.parseDouble(df.format(valor));
    }
}
