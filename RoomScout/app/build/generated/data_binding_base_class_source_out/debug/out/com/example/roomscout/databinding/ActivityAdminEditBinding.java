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

public final class ActivityAdminEditBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnGuardar;

  @NonNull
  public final Button btnVolver;

  @NonNull
  public final EditText editTextNombre;

  @NonNull
  public final EditText editTextPrecio;

  @NonNull
  public final LinearLayout mapLayout;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView tvDireccion;

  @NonNull
  public final TextView tvTitle;

  @NonNull
  public final TextView tvUbicacion;

  private ActivityAdminEditBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnGuardar,
      @NonNull Button btnVolver, @NonNull EditText editTextNombre, @NonNull EditText editTextPrecio,
      @NonNull LinearLayout mapLayout, @NonNull Toolbar toolbar, @NonNull TextView tvDireccion,
      @NonNull TextView tvTitle, @NonNull TextView tvUbicacion) {
    this.rootView = rootView;
    this.btnGuardar = btnGuardar;
    this.btnVolver = btnVolver;
    this.editTextNombre = editTextNombre;
    this.editTextPrecio = editTextPrecio;
    this.mapLayout = mapLayout;
    this.toolbar = toolbar;
    this.tvDireccion = tvDireccion;
    this.tvTitle = tvTitle;
    this.tvUbicacion = tvUbicacion;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAdminEditBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAdminEditBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_admin_edit, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAdminEditBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnGuardar;
      Button btnGuardar = ViewBindings.findChildViewById(rootView, id);
      if (btnGuardar == null) {
        break missingId;
      }

      id = R.id.btnVolver;
      Button btnVolver = ViewBindings.findChildViewById(rootView, id);
      if (btnVolver == null) {
        break missingId;
      }

      id = R.id.editTextNombre;
      EditText editTextNombre = ViewBindings.findChildViewById(rootView, id);
      if (editTextNombre == null) {
        break missingId;
      }

      id = R.id.editTextPrecio;
      EditText editTextPrecio = ViewBindings.findChildViewById(rootView, id);
      if (editTextPrecio == null) {
        break missingId;
      }

      id = R.id.mapLayout;
      LinearLayout mapLayout = ViewBindings.findChildViewById(rootView, id);
      if (mapLayout == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.tvDireccion;
      TextView tvDireccion = ViewBindings.findChildViewById(rootView, id);
      if (tvDireccion == null) {
        break missingId;
      }

      id = R.id.tvTitle;
      TextView tvTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvTitle == null) {
        break missingId;
      }

      id = R.id.tvUbicacion;
      TextView tvUbicacion = ViewBindings.findChildViewById(rootView, id);
      if (tvUbicacion == null) {
        break missingId;
      }

      return new ActivityAdminEditBinding((ConstraintLayout) rootView, btnGuardar, btnVolver,
          editTextNombre, editTextPrecio, mapLayout, toolbar, tvDireccion, tvTitle, tvUbicacion);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
