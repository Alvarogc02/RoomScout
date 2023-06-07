package rsAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roomscout.R;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.DeleteResult;
import rsAdaptadores.AdaptadorHotelesAdmin;
import rsConexion.Conexion;

import rsObjetos.Hotel;


public class AdminListActivity extends AppCompatActivity {

    private List<Hotel> listaHoteles = new ArrayList<>();


    private ListView lvHoteles;
    private AdaptadorHotelesAdmin adaptador;


    private Button btnFiltrar, btnAnadir, btnVolver;

    private String nombreHotel;

    private boolean ordenMenorAMayor = true;

    private SweetAlertDialog swal;

    // Hacer que no se pueda volver a la ventana anterior pulsando el botón del movil
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);


        btnFiltrar = this.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenarPorPrecio();
            }
        });

        btnAnadir = this.findViewById(R.id.btnAnadir);
        btnAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminListActivity.this, AdminInsertActivity.class);
                intent.putExtra("nombreHotel", nombreHotel);
                startActivity(intent);
            }
        });

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminListActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        lvHoteles = this.findViewById(R.id.lvHoteles);
        adaptador = new AdaptadorHotelesAdmin(AdminListActivity.this, listaHoteles);
        lvHoteles.setAdapter(adaptador);


        lvHoteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el nombre del hotel pulsado
                nombreHotel = listaHoteles.get(position).getNombre();

                // Crear el AlertDialog con las opciones de edición y eliminación
                new SweetAlertDialog(AdminListActivity.this)
                        .setTitleText("¿Qué desea hacer con el hotel" + nombreHotel + "?")
                                .setConfirmButtonBackgroundColor(0xFFFFA500)
                                .setConfirmButton("Editar", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                // Ir a la ventana de edición del hotel
                                Intent intent = new Intent(AdminListActivity.this, AdminEditActivity.class);
                                intent.putExtra("nombreHotel", nombreHotel);
                                startActivity(intent);

                            }
                                }).setNeutralButtonBackgroundColor(0xFFFF0000)
                                .setNeutralButton("Eliminar", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                // Crear otro AlertDialog para confirmar la eliminación del hotel
                                new SweetAlertDialog(AdminListActivity.this)
                                        .setTitleText("¿Está seguro de que desea eliminar el hotel " + nombreHotel + "?")
                                        .setConfirmButton("Sí", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                new BorrarHotelTask().execute();
                                                listaHoteles.remove(position);
                                                adaptador.notifyDataSetChanged();
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss(); // Cerrar el diálogo
                                            }
                                        })
                                        .show();
                            }
                                }).setCancelButtonBackgroundColor(0xFFD8D8D8)
                                .setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss(); // Cerrar el diálogo sin hacer nada
                            }
                        })
                        .show();
            }
        });

        // Mostrar SweetAlert de Cargando
        swal = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Cargando");
        swal.show();

        Conexion.conectarBD(AdminListActivity.this);
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

    public class BorrarHotelTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Conexion.conectarCollection("hoteles");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("ObtenerHotelesTask", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("ObtenerHotelesTask", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

            RealmResultTask<DeleteResult> findTask = Conexion.mongoCollection.deleteOne(new Document("nombre", nombreHotel));
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    Toast.makeText(AdminListActivity.this, "Hotel eliminado", Toast.LENGTH_SHORT).show();
                    // Aquí se imprime el registro de depuración DESPUÉS de que se haya actualizado la lista
                    lvHoteles.setAdapter(adaptador);
                } else {
                    Log.d("ObtenerHotelesTask", "Error al buscar hoteles");
                }
            });
        }
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