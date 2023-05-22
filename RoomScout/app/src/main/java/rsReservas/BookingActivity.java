package rsReservas;

import static rsConexion.Conexion.mongoCollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomscout.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.bson.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rsConexion.Conexion;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class BookingActivity extends AppCompatActivity {

    private TextView tvHotel, tvFechaIda, tvFechaVuelta;
    private EditText etPersonas;
    private Button btnReservar, btnFechas;

    private long fechaIda = 0, fechaVuelta = 0;
    private String hotel, nick, fechaIdaString, fechaVueltaString;
    private int precioTotal, precioNoche;
    private boolean seleccionandoFechaIda = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Bundle bundle = this.getIntent().getExtras();
        hotel = bundle.getString("hotel");
        nick = bundle.getString("nick");

        btnFechas = findViewById(R.id.btnFechas);

        btnReservar = findViewById(R.id.btnReservar);

        tvHotel = findViewById(R.id.tvHotel);
        tvFechaIda = findViewById(R.id.tvFechaIda);
        tvFechaVuelta = findViewById(R.id.tvFechaVuelta);
        etPersonas = findViewById(R.id.etPersonas);

        tvHotel.setText(hotel);

        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .build();

        btnFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "tag");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        // Obteniendo las fechas en formato long
                        fechaIda = selection.first;
                        fechaVuelta = selection.second;

                        // Procesar las fechas como desees
                        // Por ejemplo, puedes mostrar el texto en el TextView
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        fechaIdaString = sdf.format(new Date(fechaIda));
                        fechaVueltaString = sdf.format(new Date(fechaVuelta));
                        tvFechaIda.setText(fechaIdaString);
                        tvFechaVuelta.setText(fechaVueltaString);
                    }
                });
            }
        });



        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personasString = etPersonas.getText().toString();
                if (!personasString.isEmpty() && Integer.parseInt(personasString) > 0) {
                    int personas = Integer.parseInt(personasString);
                    if (fechaIda != 0 && fechaVuelta != 0) {
                        // Obtenemos la fecha actual en milisegundos
                        Calendar calendar = Calendar.getInstance();
                        long fechaActual = calendar.getTimeInMillis();
                        // Comprobamos si la fecha de ida es posterior a la fecha actual
                        if (fechaIda > fechaActual) {
                            // Obtenemos en milisegundos la diferencia entre la fechaVuelta y fechaIda y después lo dividimos entre los milisegundos que hay en un día
                            long noches = (fechaVuelta - fechaIda) / (1000 * 60 * 60 * 24);
                            Conexion.conectarCollection("hoteles");

                            RealmResultTask<MongoCursor<Document>> findTask = Conexion.mongoCollection.find(new Document("nombre", hotel)).iterator();
                            findTask.getAsync(task -> {
                                if (task.isSuccess()) {
                                    while (task.get().hasNext()) {
                                        Document resultData = task.get().next();
                                        precioNoche = resultData.getInteger("precio/noche");
                                        precioTotal = (int) (precioNoche * noches * personas);
                                    }

                                    Conexion.conectarCollection("reservas");
                                    Document reserva = new Document();
                                    reserva.append("usuario", nick)
                                            .append("hotel", hotel)
                                            .append("fecha_ida", fechaIdaString)
                                            .append("fecha_vuelta", fechaVueltaString)
                                            .append("personas", personas)
                                            .append("precio", precioTotal + "€");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                                    builder.setMessage("¿Estás seguro de que quieres hacer esta reserva?\n\n" +
                                                    "Hotel: " + hotel + "\n" +
                                                    "Fecha de ida: " + fechaIdaString + "\n" +
                                                    "Fecha de vuelta: " + fechaVueltaString + "\n" +
                                                    "Número de personas: " + personas + "\n" +
                                                    "Precio total: " + precioTotal + "€")
                                            .setTitle("Confirmar reserva")
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    mongoCollection.insertOne(reserva).getAsync(result1 -> {
                                                        if (result1.isSuccess()) {
                                                            Log.d("Prueba", "Insertado");
                                                            Toast.makeText(BookingActivity.this, "Reserva realizada", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(BookingActivity.this, AllBookingsActivity.class);
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("nick", nick);
                                                            intent.putExtras(bundle);
                                                            startActivity(intent);
                                                        } else {
                                                            Log.d("Prueba", "No Insertado");
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Toast.makeText(BookingActivity.this, "Reserva Cancelada", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        } else {
                            Toast.makeText(BookingActivity.this, "La fecha de ida debe ser posterior a la fecha actual", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BookingActivity.this, "Seleccione una fecha de ida y una fecha de regreso", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookingActivity.this, "Introduce el número de personas que van a alojarse en el hotel", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Conexion.conectarBD(BookingActivity.this);
    }
}
