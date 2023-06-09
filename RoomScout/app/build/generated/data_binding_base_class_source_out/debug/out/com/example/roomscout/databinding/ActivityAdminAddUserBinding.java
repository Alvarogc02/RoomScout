// Generated by view binder compiler. Do not edit!
package com.example.roomscout.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.roomscout.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAdminAddUserBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnSignUp;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final CheckBox cbAdmin;

  @NonNull
  public final EditText tvNick;

  @NonNull
  public final EditText tvPassword;

  @NonNull
  public final EditText tvPassword2;

  @NonNull
  public final TextView tvTitle;

  private ActivityAdminAddUserBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnSignUp,
      @NonNull CardView cardView, @NonNull CheckBox cbAdmin, @NonNull EditText tvNick,
      @NonNull EditText tvPassword, @NonNull EditText tvPassword2, @NonNull TextView tvTitle) {
    this.rootView = rootView;
    this.btnSignUp = btnSignUp;
    this.cardView = cardView;
    this.cbAdmin = cbAdmin;
    this.tvNick = tvNick;
    this.tvPassword = tvPassword;
    this.tvPassword2 = tvPassword2;
    this.tvTitle = tvTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAdminAddUserBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAdminAddUserBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_admin_add_user, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAdminAddUserBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnSignUp;
      Button btnSignUp = ViewBindings.findChildViewById(rootView, id);
      if (btnSignUp == null) {
        break missingId;
      }

      id = R.id.cardView;
      CardView cardView = ViewBindings.findChildViewById(rootView, id);
      if (cardView == null) {
        break missingId;
      }

      id = R.id.cbAdmin;
      CheckBox cbAdmin = ViewBindings.findChildViewById(rootView, id);
      if (cbAdmin == null) {
        break missingId;
      }

      id = R.id.tvNick;
      EditText tvNick = ViewBindings.findChildViewById(rootView, id);
      if (tvNick == null) {
        break missingId;
      }

      id = R.id.tvPassword;
      EditText tvPassword = ViewBindings.findChildViewById(rootView, id);
      if (tvPassword == null) {
        break missingId;
      }

      id = R.id.tvPassword2;
      EditText tvPassword2 = ViewBindings.findChildViewById(rootView, id);
      if (tvPassword2 == null) {
        break missingId;
      }

      id = R.id.tvTitle;
      TextView tvTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvTitle == null) {
        break missingId;
      }

      return new ActivityAdminAddUserBinding((ConstraintLayout) rootView, btnSignUp, cardView,
          cbAdmin, tvNick, tvPassword, tvPassword2, tvTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
