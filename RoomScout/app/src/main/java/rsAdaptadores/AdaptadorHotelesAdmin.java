package rsAdaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import rsObjetos.Hotel;
import com.example.roomscout.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorHotelesAdmin extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    private List<Hotel> listaHoteles = new ArrayList<>();
    private Button btnFavorito;
    private TextView tvNombre;
    private String nombreHotel;

    public AdaptadorHotelesAdmin(Context context, List<Hotel> listaHoteles) {
        this.context = context;
        this.listaHoteles = listaHoteles;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.adaptador_hotel_admin, null);
        TextView tvNombre = vista.findViewById(R.id.tvNombre);
        TextView tvDireccion = vista.findViewById(R.id.tvDireccion);
        TextView tvPrecio = vista.findViewById(R.id.tvPrecio);
        RatingBar rbCalificacion = vista.findViewById(R.id.rbCalificacion);

        tvNombre.setText(listaHoteles.get(position).getNombre());
        tvDireccion.setText(listaHoteles.get(position).getDireccion());
        tvPrecio.setText(String.valueOf("Precio por noche y persona: " + listaHoteles.get(position).getPrecio()) + "€");

        rbCalificacion.setProgress(listaHoteles.get(position).getValoracion());
        rbCalificacion.setIsIndicator(true); // desactiva la interacción del usuario


        return vista;
    }


    @Override
    public int getCount() {
        return listaHoteles.size();
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
