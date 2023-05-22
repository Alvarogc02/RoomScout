package rsMain;

import static rsConexion.Conexion.mongoCollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomscout.R;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import rsConexion.Conexion;
import rsObjetos.Usuario;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class SignActivity extends AppCompatActivity {

    private EditText etNick, etPassword, etPassword2;
    private Button btnSignUp;

    private List<Usuario> listaUsuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        etNick = findViewById(R.id.tvNick);
        etPassword = findViewById(R.id.tvPassword);
        etPassword2 = findViewById(R.id.tvPassword2);

        btnSignUp = findViewById(R.id.btnSignUp);

        Conexion.conectarBD(SignActivity.this);
        new ConexionTask().execute();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = etNick.getText().toString();
                String password = etPassword.getText().toString();
                String password2 = etPassword2.getText().toString();

                if(nick.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(SignActivity.this, "Ningún campo puede estar vacío", Toast.LENGTH_SHORT).show();
                } else {
                    // Comprobamos si el usuario ya existe en la base de datos
                    boolean existeUsuario = false;
                    for (Usuario usuario : listaUsuarios) {
                        if (usuario.getNick().equals(nick)) {
                            existeUsuario = true;
                            break;
                        }
                    }

                    if (existeUsuario) {
                        Toast.makeText(SignActivity.this, "Ya existe un usuario con el mismo nick", Toast.LENGTH_SHORT).show();
                    } else if (password.equals(password2)) {
                        // Insertamos el nuevo usuario en la base de datos
                        Document usuario = new Document();
                        usuario.append("nick", nick).append("password", password).append("isAdmin", false);

                        mongoCollection.insertOne(usuario).getAsync(result -> {
                            if (result.isSuccess()) {
                                Toast.makeText(SignActivity.this, "Usuario insertado correctamente", Toast.LENGTH_SHORT).show();

                                // Borramos todos los campos
                                etNick.setText(null);
                                etPassword.setText(null);
                                etPassword2.setText(null);

                                // Redireccionamos a LoginActivity para poder iniciar sesión
                                Intent intent = new Intent(SignActivity.this, LoginActivity.class);
                                startActivity(intent);

                            } else {
                                Log.d("Error", "No Insertado");
                            }
                        });
                    } else {
                        Toast.makeText(SignActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


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