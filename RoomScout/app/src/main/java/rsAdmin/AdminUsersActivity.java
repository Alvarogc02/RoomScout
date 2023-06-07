package rsAdmin;

import androidx.appcompat.app.AppCompatActivity;

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
import rsAdaptadores.AdaptadorUsuariosAdmin;
import rsConexion.Conexion;
import rsObjetos.Usuario;

public class AdminUsersActivity extends AppCompatActivity {

    private List<Usuario> listaUsuarios = new ArrayList<>();

    private ListView lvUsuarios;
    private AdaptadorUsuariosAdmin adaptador;

    private Button btnAnadirUser, btnVolver;

    private SweetAlertDialog swal;

    // Hacer que no se pueda volver a la ventana anterior pulsando el botón del movil
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        lvUsuarios = this.findViewById(R.id.lvUsuarios);
        adaptador = new AdaptadorUsuariosAdmin(AdminUsersActivity.this, listaUsuarios);
        lvUsuarios.setAdapter(adaptador);

        btnAnadirUser = findViewById(R.id.btnAnadirUser);
        btnAnadirUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUsersActivity.this, AdminAddUserActivity.class);
                startActivity(intent);
            }
        });

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUsersActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        // Mostrar SweetAlert de Cargando
        swal = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Cargando");
        swal.show();

        Conexion.conectarBD(AdminUsersActivity.this);
        new ConexionTask().execute();
    }

    public class ConexionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Conexion.conectarCollection("usuarios");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("Prueba", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("Prueba", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find().iterator();
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    while (task.get().hasNext()) {
                        Document resultData = task.get().next();

                        Usuario usuario = new Usuario(
                                resultData.getString("nick"),
                                resultData.getString("password"),
                                resultData.getBoolean("isAdmin")
                        );
                        listaUsuarios.add(usuario);
                    }

                    // Aquí se imprime el registro de depuración DESPUÉS de que se haya actualizado la lista
                    lvUsuarios.setAdapter(adaptador);

                    // Desaparecer SweetAlert de Cargando
                    swal.dismiss();
                } else {
                    Log.d("Prueba", "Error al buscar hoteles");
                }
            });
        }
    }
}