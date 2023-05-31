package rsAdmin;

import static rsConexion.Conexion.mongoCollection;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rsConexion.Conexion;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class AdminInsertActivity extends AppCompatActivity implements OnMapReadyCallback {

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

    // Hacer que no se pueda volver a la ventana anterior pulsando el botón del movil
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_insert);

        etNombre = findViewById(R.id.editTextNombre);
        etPrecio = findViewById(R.id.editTextPrecio);

        tvDireccion = findViewById(R.id.tvDireccion);

        btnVolver = findViewById(R.id.btnVolver);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Obtener el mapa asincrónicamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Conexion.conectarBD(AdminInsertActivity.this);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminInsertActivity.this, AdminListActivity.class);
                startActivity(intent);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = etNombre.getText().toString();

                if (nombre.isEmpty() || direccion.isEmpty() || etPrecio.getText().toString().isEmpty()) {
                    Toast.makeText(AdminInsertActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    precio = Integer.parseInt(etPrecio.getText().toString());
                    // Generar un número aleatorio entre 1 y 10
                    Random random = new Random();
                    int numAleatorio = random.nextInt(10) + 1;

                    // Redondear hacia abajo sin decimales
                    int valoracion = (int) Math.floor(numAleatorio);

                    List<String> arrayFav = Arrays.asList();

                    Conexion.conectarCollection("hoteles");

                    Document hotel = new Document();
                    hotel.append("nombre", nombre)
                            .append("direccion", direccion)
                            .append("precio/noche", precio)
                            .append("latitud", latitud)
                            .append("longitud", longitud)
                            .append("valoracion", valoracion)
                            .append("favorito_de", arrayFav);

                    mongoCollection.insertOne(hotel).getAsync(result -> {
                        if (result.isSuccess()) {
                            Log.d("Prueba", "Insertado");
                            Toast.makeText(AdminInsertActivity.this, "Hotel insertado correctamente", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminInsertActivity.this, AdminListActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("Prueba", "No Insertado");
                        }
                    });
                }
            }
        });
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

                Geocoder geocoder = new Geocoder(AdminInsertActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitud, longitud, 1);
                    if (addresses.size() > 0 && addresses != null) {
                        direccion = addresses.get(0).getAddressLine(0);
                        Toast.makeText(AdminInsertActivity.this, latitud + "\n" + longitud, Toast.LENGTH_SHORT).show();
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
