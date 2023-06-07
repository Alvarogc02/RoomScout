package rsAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roomscout.R;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import rsAdaptadores.AdaptadorReservas;
import rsAdaptadores.AdaptadorReservasAdmin;
import rsConexion.Conexion;
import rsMain.LoginActivity;
import rsObjetos.Reserva;
import rsReservas.AllBookingsActivity;

public class AdminAllBookingsActivity extends AppCompatActivity {

    private List<Reserva> listaReservas = new ArrayList<>();


    private ListView lvReservas;
    private AdaptadorReservasAdmin adaptador;

    private Button btnVolver;

    private SweetAlertDialog swal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_bookings);

        lvReservas = this.findViewById(R.id.lvReservas);
        adaptador = new AdaptadorReservasAdmin(AdminAllBookingsActivity.this, listaReservas);
        lvReservas.setAdapter(adaptador);

        // Mostrar SweetAlert de Cargando
        swal = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Cargando");
        swal.show();

        Conexion.conectarBD(AdminAllBookingsActivity.this);
        new ConexionTask().execute();

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAllBookingsActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });
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

            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find().iterator();
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
                    Toast.makeText(AdminAllBookingsActivity.this, "Reservas: " + listaReservas.size(), Toast.LENGTH_SHORT).show();
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
}