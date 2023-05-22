package rsAdaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roomscout.R;

import java.util.List;

import rsObjetos.Hotel;
import rsObjetos.Usuario;

public class AdaptadorUsuariosAdmin extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    private List<Usuario> listaUsuarios;

    public AdaptadorUsuariosAdmin(Context context, List<Usuario> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.adaptador_usuarios_admin, null);
        TextView tvNombre = vista.findViewById(R.id.tvNombre);
        TextView tvPassword = vista.findViewById(R.id.tvPassword);
        TextView tvAdmin = vista.findViewById(R.id.tvAdmin);

        tvNombre.setText(listaUsuarios.get(position).getNick());
        tvPassword.setText("Contraseña: " + listaUsuarios.get(position).getPassword());
        if (listaUsuarios.get(position).isAdmin()) {
            tvAdmin.setText("Es administrador: Sí");
        } else {
            tvAdmin.setText("Es administrador: No");
        }


        return vista;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
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
