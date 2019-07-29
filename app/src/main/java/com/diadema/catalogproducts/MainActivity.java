package com.diadema.catalogproducts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CEK on 22/03/2019.
 */

public class MainActivity extends AppCompatActivity implements CanadaFragment.OnFragmentInteractionListener,
                                                EstadosUnidosFragment.OnFragmentInteractionListener,
                                                EuropaFragment.OnFragmentInteractionListener,
                                                MarcasPrivadasFragment.OnFragmentInteractionListener{
    @BindView(R.id.navigation) BottomNavigationView navigation;
    private static final int PERMISION_FINE_LOCATION=100;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_canada:
                        loadFragment(new CanadaFragment());
                        return true;
                    case R.id.navigation_usa:
                        loadFragment(new EstadosUnidosFragment());
                        return true;
                    case R.id.navigation_europa:
                        loadFragment(new EuropaFragment());
                        return true;
                    case R.id.navigation_marcas_privadas:
                        loadFragment(new MarcasPrivadasFragment());
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISION_FINE_LOCATION);
                recreate();
            }
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            loadFragment(new CanadaFragment());
        }else{
            Snackbar.make(getWindow().getDecorView().getRootView(),"Debe activar los servicios de localización",Snackbar.LENGTH_LONG).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISION_FINE_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission was denied or request was cancelled
            }
        }
    }



    private void loadFragment(Fragment fragment) {
        //switching fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }
}