package rsHoteles;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.roomscout.R;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import org.bson.Document;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rsConexion.Conexion;
import rsAdaptadores.AdaptadorHoteles;
import rsMain.LoginActivity;
import rsObjetos.Hotel;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import rsReservas.AllBookingsActivity;
import rsReservas.BookingActivity;


public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<Hotel> listaHoteles = new ArrayList<>();

    private DrawerLayout drawerLayout;

    private ListView lvHoteles;
    private AdaptadorHoteles adaptador;

    private Intent intent;
    private Bundle bundle;

    private Button btnFiltrar, btnFavorito;

    private String nick;
    private boolean ordenMenorAMayor = true;

    private SweetAlertDialog swal;

    // Hacer que no se pueda volver a la ventana anterior pulsando el botón del movil
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        drawerLayout = findViewById(R.id.nav_drawer);

        btnFiltrar = this.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenarPorPrecio();
            }
        });


        Bundle bundle = this.getIntent().getExtras();
        nick = bundle.getString("nick");

        // Crear barra de tareas con menú lateral desplegable
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.cerrar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Añadir nombre de usuario al menú para saber con cuál usuario nos hemos registrado
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_usuario);
        item.setTitle(nick);

        lvHoteles = this.findViewById(R.id.lvHoteles);
        adaptador = new AdaptadorHoteles(ListActivity.this, listaHoteles, btnFavorito, nick);
        lvHoteles.setAdapter(adaptador);

        // Mostrar SweetAlert de Cargando
        swal = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Cargando");
        swal.show();

        Conexion.conectarBD(ListActivity.this);
        new ConexionTask().execute();
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

            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find().iterator();
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    while (task.get().hasNext()) {
                        Document resultData = task.get().next();
                        Log.d("ObtenerHotelesTask", "Nombre: " + resultData.getString("nombre")
                                + " | Direccion: " + resultData.getString("direccion") + " | Precio: " + resultData.getInteger("precio/noche")
                                + " | Latitud: " + resultData.getDouble("latitud") + " | Longitud: " + resultData.getDouble("longitud"));

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
                    lvHoteles.setAdapter(adaptador);
                    // Desaparecer SweetAlert de Cargando
                    swal.dismiss();
                } else {
                    Log.d("ObtenerHotelesTask", "Error al buscar hoteles");
                }
            });
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


    private void ordenarPorPrecio() {
        if (ordenMenorAMayor) {
            // Ordenar de menor a mayor precio
            Collections.sort(listaHoteles, new Comparator<Hotel>() {
                @Override
                public int compare(Hotel h1, Hotel h2) {
                    return Integer.compare(h1.getPrecio(), h2.getPrecio());
                }
            });
            ordenMenorAMayor = false;
        } else {
            // Ordenar de mayor a menor precio
            Collections.sort(listaHoteles, new Comparator<Hotel>() {
                @Override
                public int compare(Hotel h1, Hotel h2) {
                    return Integer.compare(h2.getPrecio(), h1.getPrecio());
                }
            });
            ordenMenorAMayor = true;
        }
        adaptador.notifyDataSetChanged();
    }
}