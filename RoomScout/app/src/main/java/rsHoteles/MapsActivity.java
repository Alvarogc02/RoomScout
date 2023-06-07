package rsHoteles;

import static rsConexion.Conexion.mongoCollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.roomscout.R;
import com.example.roomscout.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;


import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rsConexion.Conexion;
import rsMain.LoginActivity;
import rsObjetos.Hotel;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import rsReservas.AllBookingsActivity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private DrawerLayout drawerLayout;

    private Intent intent;
    private Bundle bundle;

    private List<Hotel> listaHoteles = new ArrayList<>();

    private String nick;
    private String nombreHotel;
    private int precio;
    private double latitud;
    private double longitud;

    private SweetAlertDialog swal;

    // Hacer que no se pueda volver a la ventana anterior pulsando el botón del movil
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = this.getIntent().getExtras();
        nick = bundle.getString("nick");

        // Crear barra de tareas con menú lateral desplegable
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("RoomScout");
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.nav_drawer); // initialize drawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.cerrar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Añadir nombre de usuario al menú para saber con cuál usuario nos hemos registrado
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_usuario);
        item.setTitle(nick);

        // Mostrar SweetAlert de Cargando
        swal = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Cargando");
        swal.show();

        Conexion.conectarBD(MapsActivity.this);
        new ConexionTask().execute();
    }

    public class ConexionTask extends AsyncTask<Void, Void, List<Hotel>> {

        @Override
        protected List<Hotel> doInBackground(Void... voids) {
            Conexion.conectarCollection("hoteles");
            return null;
        }

        @Override
        protected void onPostExecute(List<Hotel> result) {
            Log.d("ObtenerHotelesTask", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("ObtenerHotelesTask", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

            Document queryFilter = new Document();
            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
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
                        listaHoteles.add(hotel);
                    }
                    // Aquí se imprime el registro de depuración DESPUÉS de que se haya actualizado la lista
                    Log.d("ObtenerHotelesTask", "Hoteles encontrados " + listaHoteles.size());
                    onMapReady(mMap);

                    // Desaparecer SweetAlert de Cargando
                    swal.dismiss();
                } else {
                    Log.d("ObtenerHotelesTask", "Error al buscar hoteles");
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng centro = new LatLng(39.42, -3.40);
        float zoomLevel = 5.35f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(centro, zoomLevel);
        mMap.moveCamera(cameraUpdate);

        for(int i=0; i<listaHoteles.size(); i++) {
            nombreHotel = listaHoteles.get(i).getNombre();
            precio = listaHoteles.get(i).getPrecio();
            latitud = listaHoteles.get(i).getLatitud();
            longitud = listaHoteles.get(i).getLongitud();

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
            int newWidth = 60;
            int newHeight = 90;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);

            LatLng marcador = new LatLng(latitud, longitud);
            mMap.addMarker(new MarkerOptions().position(marcador).title(nombreHotel).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_hoteles:
                intent = new Intent(getApplicationContext(), ListActivity.class);
                break;
            case R.id.nav_map:
                intent = new Intent(getApplicationContext(), MapsActivity.class);
                break;
            case R.id.nav_misreservas:
                intent = new Intent(getApplicationContext(), AllBookingsActivity.class);
                break;
            case R.id.nav_favs:
                intent = new Intent(getApplicationContext(), FavsActivity.class);
                break;
            case R.id.nav_exit:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                break;
        }

        bundle = new Bundle();
        bundle.putString("nick", nick);
        intent.putExtras(bundle);
        startActivity(intent);
        return false;
    }

}