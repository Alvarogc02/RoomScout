package rsAdaptadores;


import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;
import static rsConexion.Conexion.mongoCollection;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import rsConexion.Conexion;
import rsHoteles.FavsActivity;
import rsObjetos.Hotel;
import rsReservas.BookingActivity;

import com.example.roomscout.R;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdaptadorHoteles extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    private List<Hotel> listaHoteles = new ArrayList<>();
    private Button btnFavorito;
    private TextView tvNombre;
    private String nombreHotel;
    private String nick;
    private boolean isFavorito;

    public AdaptadorHoteles(Context context, List<Hotel> listaHoteles, Button btnFavorito, String nick) {
        this.context = context;
        this.listaHoteles = listaHoteles;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.btnFavorito = btnFavorito;
        this.nick = nick;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.adaptador_hotel, null);
        TextView tvNombre = vista.findViewById(R.id.tvNombre);
        TextView tvDireccion = vista.findViewById(R.id.tvDireccion);
        TextView tvPrecio = vista.findViewById(R.id.tvPrecio);
        RatingBar rbCalificacion = vista.findViewById(R.id.rbCalificacion);
        Button btnFavorito = vista.findViewById(R.id.btnFavorito);

        tvNombre.setText(listaHoteles.get(position).getNombre());
        tvDireccion.setText(listaHoteles.get(position).getDireccion());
        tvPrecio.setText("Precio por noche y persona: " + listaHoteles.get(position).getPrecio() + "€");

        rbCalificacion.setProgress(listaHoteles.get(position).getValoracion());
        rbCalificacion.setIsIndicator(true);  // desactiva la interacción del usuario

        // Actualizar icono del botón de favoritos
        List<String> favoritos = Arrays.asList(listaHoteles.get(position).getFavorito_de());
        final boolean[] isFavorito = {favoritos != null && favoritos.contains(nick)};
        if (isFavorito[0]) {
            btnFavorito.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favon));
        } else {
            btnFavorito.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favoff));
        }

        Conexion.conectarBD(context);

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorito[0] = !isFavorito[0];
                nombreHotel = listaHoteles.get(position).getNombre();
                if (isFavorito[0]) {
                    btnFavorito.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favon));
                    new AddFavorito().execute();
                } else {
                    btnFavorito.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favoff));
                    new DeleteFavorito().execute();
                    if (context.getClass().getName().equals("rsHoteles.FavsActivity")) {
                        eliminarHotel(position);
                    }
                }
            }
        });

        vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre del hotel pulsado
                String nombreHotel = listaHoteles.get(position).getNombre();

                Intent intent = new Intent(context, BookingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hotel", nombreHotel);
                bundle.putString("nick", nick);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return vista;
    }


    public class AddFavorito extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Conexion.conectarCollection("hoteles");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("ObtenerHotelesTask", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("ObtenerHotelesTask", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find(new Document("nombre", nombreHotel)).iterator();
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    Document updateDocument = new Document().append("$addToSet", new Document().append("favorito_de", nick));

                    mongoCollection.updateOne(new Document("nombre", nombreHotel), updateDocument).getAsync(updateTask -> {
                        if (updateTask.isSuccess()) {
                            Toast.makeText(context, "Hotel " + nombreHotel + " añadido a favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d("ObtenerHotelesTask", "Error al buscar hoteles");
                }
            });
        }
    }

    public class DeleteFavorito extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Conexion.conectarCollection("hoteles");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("ObtenerHotelesTask", "Conectado a la BBDD " + Conexion.mongoDatabase.getName());
            Log.d("ObtenerHotelesTask", "Conectado a la coleccion " + Conexion.mongoCollection.getName());

            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find(new Document("nombre", nombreHotel)).iterator();
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    Document updateDocument = new Document().append("$pull", new Document().append("favorito_de", nick));

                    mongoCollection.updateOne(new Document("nombre", nombreHotel), updateDocument).getAsync(updateTask -> {
                        if (updateTask.isSuccess()) {
                            Toast.makeText(context, "Hotel " + nombreHotel + " eliminado de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d("ObtenerHotelesTask", "Error al buscar hoteles");
                }
            });
        }
    }

    public void eliminarHotel(int posicion) {
        listaHoteles.remove(posicion);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return listaHoteles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}