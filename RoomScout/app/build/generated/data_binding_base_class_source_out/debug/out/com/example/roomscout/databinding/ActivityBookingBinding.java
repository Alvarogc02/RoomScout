// Generated by view binder compiler. Do not edit!
package com.example.roomscout.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.roomscout.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityBookingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnFechas;

  @NonNull
  public final Button btnReservar;

  @NonNull
  public final Button btnVolver;

  @NonNull
  public final EditText etPersonas;

  @NonNull
  public final LinearLayout llFechas;

  @NonNull
  public final LinearLayout llFechasSeleccionadas;

  @NonNull
  public final LinearLayout llPersonas;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView tvFechaIda;

  @NonNull
  public final TextView tvFechaVuelta;

  @NonNull
  public final TextView tvHotel;

  private ActivityBookingBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnFechas,
      @NonNull Button btnReservar, @NonNull Button btnVolver, @NonNull EditText etPersonas,
      @NonNull LinearLayout llFechas, @NonNull LinearLayout llFechasSeleccionadas,
      @NonNull LinearLayout llPersonas, @NonNull Toolbar toolbar, @NonNull TextView tvFechaIda,
      @NonNull TextView tvFechaVuelta, @NonNull TextView tvHotel) {
    this.rootView = rootView;
    this.btnFechas = btnFechas;
    this.btnReservar = btnReservar;
    this.btnVolver = btnVolver;
    this.etPersonas = etPersonas;
    this.llFechas = llFechas;
    this.llFechasSeleccionadas = llFechasSeleccionadas;
    this.llPersonas = llPersonas;
    this.toolbar = toolbar;
    this.tvFechaIda = tvFechaIda;
    this.tvFechaVuelta = tvFechaVuelta;
    this.tvHotel = tvHotel;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBookingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBookingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_booking, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBookingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnFechas;
      Button btnFechas = ViewBindings.findChildViewById(rootView, id);
      if (btnFechas == null) {
        break missingId;
      }

      id = R.id.btnReservar;
      Button btnReservar = ViewBindings.findChildViewById(rootView, id);
      if (btnReservar == null) {
        break missingId;
      }

      id = R.id.btnVolver;
      Button btnVolver = ViewBindings.findChildViewById(rootView, id);
      if (btnVolver == null) {
        break missingId;
      }

      id = R.id.etPersonas;
      EditText etPersonas = ViewBindings.findChildViewById(rootView, id);
      if (etPersonas == null) {
        break missingId;
      }

      id = R.id.llFechas;
      LinearLayout llFechas = ViewBindings.findChildViewById(rootView, id);
      if (llFechas == null) {
        break missingId;
      }

      id = R.id.llFechasSeleccionadas;
      LinearLayout llFechasSeleccionadas = ViewBindings.findChildViewById(rootView, id);
      if (llFechasSeleccionadas == null) {
        break missingId;
      }

      id = R.id.llPersonas;
      LinearLayout llPersonas = ViewBindings.findChildViewById(rootView, id);
      if (llPersonas == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.tvFechaIda;
      TextView tvFechaIda = ViewBindings.findChildViewById(rootView, id);
      if (tvFechaIda == null) {
        break missingId;
      }

      id = R.id.tvFechaVuelta;
      TextView tvFechaVuelta = ViewBindings.findChildViewById(rootView, id);
      if (tvFechaVuelta == null) {
        break missingId;
      }

      id = R.id.tvHotel;
      TextView tvHotel = ViewBindings.findChildViewById(rootView, id);
      if (tvHotel == null) {
        break missingId;
      }

      return new ActivityBookingBinding((ConstraintLayout) rootView, btnFechas, btnReservar,
          btnVolver, etPersonas, llFechas, llFechasSeleccionadas, llPersonas, toolbar, tvFechaIda,
          tvFechaVuelta, tvHotel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
