package rsReservas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rsHoteles.FavsActivity;
import rsHoteles.ListActivity;
import rsHoteles.MapsActivity;
import com.example.roomscout.R;
import com.google.android.material.navigation.NavigationView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import rsConexion.Conexion;
import rsAdaptadores.AdaptadorReservas;
import rsMain.LoginActivity;
import rsObjetos.Reserva;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class AllBookingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private List<Reserva> listaReservas = new ArrayList<>();

    private DrawerLayout drawerLayout;

    private ListView lvReservas;
    private AdaptadorReservas adaptador;

    private Intent intent;
    private Bundle bundle;

    private String nick;

    private SweetAlertDialog swal;

    // Hacer que no se pueda volver a la ventana anterior pulsando el botón del movil
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bookings);

        drawerLayout = findViewById(R.id.nav_drawer);

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

        lvReservas = this.findViewById(R.id.lvReservas);
        adaptador = new AdaptadorReservas(AllBookingsActivity.this, listaReservas);
        lvReservas.setAdapter(adaptador);

        // Mostrar SweetAlert de Cargando
        swal = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Cargando");
        swal.show();

        Conexion.conectarBD(AllBookingsActivity.this);
        new ConexionTask().execute();
    }

    public class ConexionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Conexion.conectarCollection("reservas");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("ObtenerHotelesTask", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("ObtenerHotelesTask", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find(new Document("usuario", nick)).iterator();
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    while (task.get().hasNext()) {
                        Document resultData = task.get().next();

                        Reserva reserva = new Reserva(
                                resultData.getString("usuario"),
                                resultData.getString("hotel"),
                                resultData.getString("fecha_ida"),
                                resultData.getString("fecha_vuelta"),
                                resultData.getInteger("personas"),
                                resultData.getString("precio")
                        );
                        listaReservas.add(reserva);
                    }
                    Toast.makeText(AllBookingsActivity.this, "Reservas: " + listaReservas.size(), Toast.LENGTH_SHORT).show();
                    // Aquí se imprime el registro de depuración DESPUÉS de que se haya actualizado la lista
                    lvReservas.setAdapter(adaptador);

                    // Desaparecer SweetAlert de Cargando
                    swal.dismiss();
                } else {
                    Log.d("Error", "Error al buscar reservas");
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
}