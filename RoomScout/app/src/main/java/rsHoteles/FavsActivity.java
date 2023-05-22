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

import rsConexion.Conexion;
import rsAdaptadores.AdaptadorHoteles;
import rsMain.LoginActivity;
import rsObjetos.Hotel;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import rsReservas.AllBookingsActivity;
import rsReservas.BookingActivity;


public class FavsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<Hotel> listaHoteles = new ArrayList<>();

    private DrawerLayout drawerLayout;

    private ListView lvHoteles;
    private AdaptadorHoteles adaptador;

    private Intent intent;
    private Bundle bundle;

    private Button btnFiltrar, btnFavorito;

    private String nombreHotel;

    private String nick;
    private boolean ordenMenorAMayor = true;

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
        adaptador = new AdaptadorHoteles(FavsActivity.this, listaHoteles, btnFavorito, nick);
        lvHoteles.setAdapter(adaptador);


        lvHoteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el nombre del hotel pulsado
                String nombreHotel = listaHoteles.get(position).getNombre();

                intent = new Intent(getApplicationContext(), BookingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hotel", nombreHotel);
                bundle.putString("nick", nick);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });


        Conexion.conectarBD(FavsActivity.this);
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
            // Se crea una tarea asincrónica que realiza una consulta en la colección de MongoDB a través de la conexión existente

            findTask.getAsync(task -> {
                // Una vez que la tarea se ha completado, se verifica si se ha producido algún error
                if (task.isSuccess()) {
                    // Si no hay errores, se itera sobre los resultados de la consulta
                    while (task.get().hasNext()) {
                        // Para cada documento resultante, se crea un objeto Hotel con los valores correspondientes
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
                        // Se establece una variable "agregarHotel" en false y se verifica si nick está en la lista de favoritos
                        boolean agregarHotel = false;
                        for (int j = 0; j < listaFav.size(); j++) {
                            if (listaFav.get(j).equals(nick)) {
                                agregarHotel = true;
                                break;
                            }
                        }
                        // Si nick está en la lista de favoritos, se agrega el objeto Hotel a la lista "listaHoteles"
                        if (agregarHotel) {
                            listaHoteles.add(hotel);
                        }
                    }

                    // Una vez que se han agregado todos los hoteles a la lista, se actualiza la interfaz de usuario con los resultados
                    lvHoteles.setAdapter(adaptador);
                    // Se itera sobre cada hotel de la lista para imprimir un registro de depuración en la consola de Android si contiene "nick" en su lista de favoritos
                    for (int i = 0; i < listaHoteles.size(); i++) {
                        for (int j = 0; j < listaHoteles.get(i).getFavorito_de().length; j++) {
                            if (listaHoteles.get(i).getFavorito_de()[j].equals("nick")) {
                                Log.d("Prueba", "Nick encontrado en " + listaHoteles.get(i).getNombre());
                                break;
                            }
                        }
                    }
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