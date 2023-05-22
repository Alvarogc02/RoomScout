package rsAdmin;

import static rsConexion.Conexion.mongoCollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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


public class AdminAddUserActivity extends AppCompatActivity {

    private EditText etNick, etPassword, etPassword2;
    private CheckBox cbAdmin;
    private Button btnSignUp;

    private boolean isAdmin = false;

    private List<Usuario> listaUsuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        etNick = findViewById(R.id.tvNick);
        etPassword = findViewById(R.id.tvPassword);
        etPassword2 = findViewById(R.id.tvPassword2);
        cbAdmin = findViewById(R.id.cbAdmin);

        btnSignUp = findViewById(R.id.btnSignUp);

        Conexion.conectarBD(AdminAddUserActivity.this);
        new ConexionTask().execute();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = etNick.getText().toString();
                String password = etPassword.getText().toString();
                String password2 = etPassword2.getText().toString();

                if(nick.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(AdminAddUserActivity.this, "Ningún campo puede estar vacío", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AdminAddUserActivity.this, "Ya existe un usuario con el mismo nick", Toast.LENGTH_SHORT).show();
                    } else if (password.equals(password2)) {
                        if (cbAdmin.isChecked()) {
                            isAdmin = true;
                        } else {
                            isAdmin = false;
                        }
                        // Insertamos el nuevo usuario en la base de datos
                        Document usuario = new Document();
                        usuario.append("nick", nick).append("password", password).append("isAdmin", isAdmin);

                        mongoCollection.insertOne(usuario).getAsync(result -> {
                            if (result.isSuccess()) {
                                Toast.makeText(AdminAddUserActivity.this, "Usuario insertado correctamente", Toast.LENGTH_SHORT).show();

                                // Borramos todos los campos
                                etNick.setText(null);
                                etPassword.setText(null);
                                etPassword2.setText(null);

                                Intent intent = new Intent(AdminAddUserActivity.this, AdminUsersActivity.class);
                                startActivity(intent);

                            } else {
                                Log.d("Error", "No Insertado");
                            }
                        });
                    } else {
                        Toast.makeText(AdminAddUserActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
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