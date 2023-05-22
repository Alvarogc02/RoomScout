// Generated by view binder compiler. Do not edit!
package com.example.roomscout.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.roomscout.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AdaptadorHotelAdminBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RatingBar rbCalificacion;

  @NonNull
  public final TextView tvDireccion;

  @NonNull
  public final TextView tvNombre;

  @NonNull
  public final TextView tvPrecio;

  private AdaptadorHotelAdminBinding(@NonNull ConstraintLayout rootView,
      @NonNull RatingBar rbCalificacion, @NonNull TextView tvDireccion, @NonNull TextView tvNombre,
      @NonNull TextView tvPrecio) {
    this.rootView = rootView;
    this.rbCalificacion = rbCalificacion;
    this.tvDireccion = tvDireccion;
    this.tvNombre = tvNombre;
    this.tvPrecio = tvPrecio;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AdaptadorHotelAdminBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AdaptadorHotelAdminBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.adaptador_hotel_admin, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AdaptadorHotelAdminBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rbCalificacion;
      RatingBar rbCalificacion = ViewBindings.findChildViewById(rootView, id);
      if (rbCalificacion == null) {
        break missingId;
      }

      id = R.id.tvDireccion;
      TextView tvDireccion = ViewBindings.findChildViewById(rootView, id);
      if (tvDireccion == null) {
        break missingId;
      }

      id = R.id.tvNombre;
      TextView tvNombre = ViewBindings.findChildViewById(rootView, id);
      if (tvNombre == null) {
        break missingId;
      }

      id = R.id.tvPrecio;
      TextView tvPrecio = ViewBindings.findChildViewById(rootView, id);
      if (tvPrecio == null) {
        break missingId;
      }

      return new AdaptadorHotelAdminBinding((ConstraintLayout) rootView, rbCalificacion,
          tvDireccion, tvNombre, tvPrecio);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
