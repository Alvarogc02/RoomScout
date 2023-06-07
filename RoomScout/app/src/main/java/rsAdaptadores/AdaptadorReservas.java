package rsAdaptadores;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomscout.R;

import org.bson.Document;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.result.DeleteResult;
import rsConexion.Conexion;
import rsObjetos.Reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorReservas extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private List<Reserva> listaReservas = new ArrayList<>();
    private Date currentDate;

    public AdaptadorReservas(Context context, List<Reserva> listaReservas) {
        this.context = context;
        this.listaReservas = listaReservas;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        currentDate = new Date(); // Obtener la fecha actual
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.adaptador_reserva_admin, null);
        TextView tvNombreHotel = vista.findViewById(R.id.tvNombreHotel);
        TextView tvFechaIda = vista.findViewById(R.id.tvFechaIda);
        TextView tvFechaVuelta = vista.findViewById(R.id.tvFechaVuelta);
        TextView tvPersonas = vista.findViewById(R.id.tvPersonas);
        TextView tvPrecio = vista.findViewById(R.id.tvPrecio);
        TextView tvUsuario = vista.findViewById(R.id.tvUsuario);
        Button btnCancelar = vista.findViewById(R.id.btnCancelar);

        // Verificar si la fecha actual es anterior a la fecha de ida
        String fechaIdaStr = listaReservas.get(position).getFechaIda();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fechaIda = null;
        try {
            fechaIda = sdf.parse(fechaIdaStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentDate.before(fechaIda)) {
            btnCancelar.setVisibility(View.VISIBLE);
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(context).setTitleText("¿Desea cancelar la reserva?")
                            .setConfirmButton("Sí", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    // Eliminar reserva
                                    new CancelarReservaTask(position).execute();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }
            });
        } else {
            btnCancelar.setVisibility(View.GONE);
        }

        tvNombreHotel.setText("Hotel: " + listaReservas.get(position).getHotel());
        tvFechaIda.setText("Ida: " + listaReservas.get(position).getFechaIda());
        tvFechaVuelta.setText("Vuelta: " + listaReservas.get(position).getFechaVuelta());
        tvPersonas.setText("Nº personas: " + listaReservas.get(position).getPersonas());
        tvPrecio.setText("Total:" + listaReservas.get(position).getPrecio());
        tvUsuario.setText("Usuario: " + listaReservas.get(position).getUsuario());

        return vista;
    }

    public class CancelarReservaTask extends AsyncTask<Void, Void, Void> {

        private int position;

        public CancelarReservaTask(int position) {
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Conexion.conectarCollection("reservas");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            RealmResultTask<DeleteResult> findTask = Conexion.mongoCollection.deleteOne(
                    new Document("usuario", listaReservas.get(position).getUsuario())
                            .append("hotel", listaReservas.get(position).getHotel())
                            .append("fecha_ida", listaReservas.get(position).getFechaIda())
                            .append("fecha_vuelta", listaReservas.get(position).getFechaVuelta())
                            .append("personas", listaReservas.get(position).getPersonas())
                            .append("precio", listaReservas.get(position).getPrecio()));
            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    Toast.makeText(context, "Reserva cancelada", Toast.LENGTH_SHORT).show();
                    listaReservas.remove(position);
                    notifyDataSetChanged();
                } else {
                    Log.d("Prueba", "Error al buscar reservas");
                }
            });
        }
    }

    @Override
    public int getCount() {
        return listaReservas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
