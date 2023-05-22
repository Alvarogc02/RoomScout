package rsAdaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roomscout.R;
import rsObjetos.Reserva;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorReservasAdmin extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    private List<Reserva> listaReservas = new ArrayList<>();

    public AdaptadorReservasAdmin(Context context, List<Reserva> listaReservas) {
        this.context = context;
        this.listaReservas = listaReservas;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

        tvNombreHotel.setText("Hotel: " + listaReservas.get(position).getHotel());
        tvFechaIda.setText("Ida: " + listaReservas.get(position).getFechaIda());
        tvFechaVuelta.setText("Vuelta: " + listaReservas.get(position).getFechaVuelta());
        tvPersonas.setText("Nº personas: " + listaReservas.get(position).getPersonas());
        tvPrecio.setText("Total:" + listaReservas.get(position).getPrecio());
        tvUsuario.setText("Usuario: " + listaReservas.get(position).getUsuario());

        return vista;
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
