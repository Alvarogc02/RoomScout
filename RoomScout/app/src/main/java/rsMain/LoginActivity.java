package rsMain;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
;

import rsAdmin.AdminMainActivity;
import rsHoteles.ListActivity;

import com.example.roomscout.R;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import rsConexion.Conexion;
import rsAdmin.AdminInsertActivity;
import rsObjetos.Usuario;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasenia;
    private Button btnInicioSesion;
    private Intent intent;
    Bundle bundle = new Bundle();


    private List<Usuario> listaUsuarios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = this.findViewById(R.id.etNick);
        etContrasenia = this.findViewById(R.id.etPassword);

        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, SignActivity.class);
                startActivity(intent);
            }
        });


        btnInicioSesion = this.findViewById(R.id.btnInicioSesion);

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nick = etUsuario.getText().toString();
                    String password = etContrasenia.getText().toString();

                    boolean credencialesValidas = false;
                    boolean isAdmin = false;
                    for (Usuario usuario : listaUsuarios) {
                        if (nick.equals(usuario.getNick()) && password.equals(usuario.getPassword())) {
                            credencialesValidas = true;
                            if (usuario.isAdmin()) {
                                isAdmin = true;
                            }
                            break;
                        }
                    }

                    if (credencialesValidas) {
                        if (isAdmin) {
                            intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, ListActivity.class);
                        }

                        bundle.putString("nick", nick);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }

                    intent.putExtras(bundle);
                    startActivity(intent);

                } catch (Exception e) {
                    // Mostrar el mensaje de error
                    Log.d("Error", "ERROR: " + e);
                }
            }
        });

        Conexion.conectarBD(LoginActivity.this);
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
            Log.d("ObtenerHotelesTask", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("ObtenerHotelesTask", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

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
                }
            });
        }
    }
}