package rsAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roomscout.R;

import rsMain.LoginActivity;

public class AdminMainActivity extends AppCompatActivity {

    private Button btnHoteles, btnUsuarios, btnReservas, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        btnHoteles = findViewById(R.id.btnHoteles);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnReservas = findViewById(R.id.btnReservas);
        btnVolver = findViewById(R.id.btnVolver);


        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.btnHoteles:
                        Intent intentHoteles = new Intent(AdminMainActivity.this, AdminListActivity.class);
                        startActivity(intentHoteles);
                        break;
                    case R.id.btnUsuarios:
                        Intent intentUsuarios = new Intent(AdminMainActivity.this, AdminUsersActivity.class);
                        startActivity(intentUsuarios);
                        break;
                    case R.id.btnReservas:
                        Intent intentReservas = new Intent(AdminMainActivity.this, AdminAllBookingsActivity.class);
                        startActivity(intentReservas);
                        break;
                    case R.id.btnVolver:
                        Intent intentVolver = new Intent(AdminMainActivity.this, LoginActivity.class);
                        startActivity(intentVolver);
                        break;
                    default:
                        break;
                }
            }
        };

        btnHoteles.setOnClickListener(buttonClickListener);
        btnUsuarios.setOnClickListener(buttonClickListener);
        btnReservas.setOnClickListener(buttonClickListener);
        btnVolver.setOnClickListener(buttonClickListener);
    }
}