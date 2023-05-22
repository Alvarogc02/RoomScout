// Generated by view binder compiler. Do not edit!
package com.example.roomscout.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.roomscout.R;
import com.google.android.material.navigation.NavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityListBinding implements ViewBinding {
  @NonNull
  private final DrawerLayout rootView;

  @NonNull
  public final Button btnFiltrar;

  @NonNull
  public final ListView lvHoteles;

  @NonNull
  public final DrawerLayout navDrawer;

  @NonNull
  public final NavigationView navView;

  @NonNull
  public final Toolbar toolbar;

  private ActivityListBinding(@NonNull DrawerLayout rootView, @NonNull Button btnFiltrar,
      @NonNull ListView lvHoteles, @NonNull DrawerLayout navDrawer, @NonNull NavigationView navView,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.btnFiltrar = btnFiltrar;
    this.lvHoteles = lvHoteles;
    this.navDrawer = navDrawer;
    this.navView = navView;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public DrawerLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnFiltrar;
      Button btnFiltrar = ViewBindings.findChildViewById(rootView, id);
      if (btnFiltrar == null) {
        break missingId;
      }

      id = R.id.lvHoteles;
      ListView lvHoteles = ViewBindings.findChildViewById(rootView, id);
      if (lvHoteles == null) {
        break missingId;
      }

      DrawerLayout navDrawer = (DrawerLayout) rootView;

      id = R.id.nav_view;
      NavigationView navView = ViewBindings.findChildViewById(rootView, id);
      if (navView == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityListBinding((DrawerLayout) rootView, btnFiltrar, lvHoteles, navDrawer,
          navView, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
