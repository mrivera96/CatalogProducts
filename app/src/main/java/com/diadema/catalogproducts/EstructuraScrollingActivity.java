package com.diadema.catalogproducts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.diadema.catalogproducts.Adapters.RecyclerViewAdapterEstructura;
import com.diadema.catalogproducts.entities.ApiError;
import com.diadema.catalogproducts.entities.Estructura;
import com.diadema.catalogproducts.entities.EstructuraResponse;
import com.diadema.catalogproducts.services.ApiService;
import com.diadema.catalogproducts.services.NetworkStatusManager;
import com.diadema.catalogproducts.services.RetrofitBuilder;
import com.diadema.catalogproducts.services.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by César Andrade on 26/03/2019.
 */

public class EstructuraScrollingActivity extends AppCompatActivity {
    private static final String TAG = "DetalleScrollingAct";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterEstructura adapter;
    private String sku;
    private String product_tmpl_id;
    private String imagenString;
    protected static ArrayList<Estructura> listaEstructura;
    Call<EstructuraResponse> callEstructura;
    ApiService service;
    private ProgressBar progressBarEstructura;
    private ConstraintLayout contenedorPadreEstructura;
    private LinearLayout contenedorMensajeEstructura, contenedorRecycleListEstructura;
    private ImageView imagenMensaje;
    private TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estructura_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            product_tmpl_id = parametros.getString("product_tmpl_id");
            imagenString = parametros.getString("imagen");
            sku = parametros.getString("sku");
        }

        setTitle(sku);
        CollapsingToolbarLayout imagenProductCollapse = findViewById(R.id.toolbar_layout);
        if (imagenString.isEmpty()){
            imagenProductCollapse.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.sin_imagen));
        }else {
            Drawable imagen = base64ToDrawable(imagenString);
            imagenProductCollapse.setBackground(imagen);
        }

        recyclerView = findViewById(R.id.recycle_list_estructura);
        progressBarEstructura = findViewById(R.id.progressBarEstructura);
        contenedorPadreEstructura = findViewById(R.id.contenedorPadreEstructura);
        contenedorMensajeEstructura = findViewById(R.id.contenedorMensajeEstructura);
        contenedorRecycleListEstructura = findViewById(R.id.contenedorRecycleListEstructura);
        imagenMensaje = findViewById(R.id.imagenMensajeEstructura);
        tvMensaje = findViewById(R.id.tvMensajeEstructura);

        switch (NetworkStatusManager.status(Objects.requireNonNull(getApplicationContext()))) {
            case "Offline":
                showMensaje(getResources().getDrawable(R.drawable.no_wifi), getString(R.string.sin_internet));
                break;
            case "Mobile data":
                showMensaje(getResources().getDrawable(R.drawable.no_wifi), getString(R.string.no_wifi));
                break;
            case "SSID Incorrect":
                showMensaje(getResources().getDrawable(R.drawable.no_wifi), getString(R.string.no_coorporativa));
                break;
            case "SSID Correct":
                getListEstructura();
                break;
        }
    }

    public void getListEstructura(){
        Log.d(TAG, "initRecyclerView: init getListEstructura.");
        showLoading();
        listaEstructura = new ArrayList<>();
        callEstructura = service.estructura(product_tmpl_id);
        //Toast.makeText(getApplicationContext(),"Inicio getListProducts CanadaFragment", Toast.LENGTH_SHORT).show();
        callEstructura.enqueue(new Callback<EstructuraResponse>() {
            @Override
            public void onResponse(@NonNull Call<EstructuraResponse> call, @NonNull Response<EstructuraResponse> response) {
                Log.w(TAG, "onResponse: " + response );

                //Toast.makeText(getApplicationContext(),"Hubo respuesta getListProducts CanadaFragment", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getData().size() == 0 ){
                        Toast.makeText(getApplicationContext(),"Sin data", Toast.LENGTH_LONG).show();
                    }else {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            listaEstructura.add(get(response.body().getData().get(i).getId(),
                                    response.body().getData().get(i).getProduct_id()));
                        }

                        if (response.body().getData().size() == listaEstructura.size()){
                            showRecycleList();
                            final LayoutAnimationController controller =
                                    AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_right_to_left);
                            adapter = new RecyclerViewAdapterEstructura(getApplicationContext(), listaEstructura);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutAnimation(controller);
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                        }else
                            Toast.makeText(getApplicationContext(), "Por favor vuelva a intentarlo.",Toast.LENGTH_LONG).show();
                    }
                }else {
                    //handleErrors(response.errorBody());
                    assert response.errorBody() != null;
                    try {
                        switch (response.message()){
                            case "Internal Server Error":
                                showMensaje(getResources().getDrawable(R.drawable.sin_respuesta), getString(R.string.error_500));
                                break;
                            default:
                                showMensaje(getResources().getDrawable(R.drawable.sin_respuesta), getString(R.string.sin_respuesta));
                                break;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EstructuraResponse> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                    String failure = t.getMessage();
                    if (failure.contains("Internal Server Error")){
                        showMensaje(getResources().getDrawable(R.drawable.sin_respuesta), getString(R.string.error_500));
                    }

                    if (failure.contains("Expected BEGIN_ARRAY")){
                        showMensaje(getResources().getDrawable(R.drawable.sin_respuesta),getString(R.string.deformed_data));
                    }else
                        showMensaje(getResources().getDrawable(R.drawable.sin_respuesta), getString(R.string.sin_respuesta));
                } catch (Exception e){
                    getListEstructura();
                }
            }
        });
    }

    private Estructura get(String id, String[] estructura){
        return new Estructura(id, estructura);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callEstructura != null) {
            callEstructura.cancel();
            callEstructura = null;
        }
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(contenedorPadreEstructura);
        contenedorRecycleListEstructura.setVisibility(View.GONE);
        contenedorMensajeEstructura.setVisibility(View.GONE);
        progressBarEstructura.setVisibility(View.VISIBLE);
    }

    private void showRecycleList(){
        contenedorRecycleListEstructura.setVisibility(View.VISIBLE);
        progressBarEstructura.setVisibility(View.GONE);
    }

    private void showMensaje(Drawable imagMensaje, String mensaje){
        imagenMensaje.setImageDrawable(imagMensaje);
        tvMensaje.setText(mensaje);
        TransitionManager.beginDelayedTransition(contenedorPadreEstructura);
        contenedorMensajeEstructura.setVisibility(View.VISIBLE);
        contenedorRecycleListEstructura.setVisibility(View.GONE);
        progressBarEstructura.setVisibility(View.GONE);
    }

    private Drawable base64ToDrawable(String imagen_mediun){
        byte[] decodedString = Base64.decode(imagen_mediun, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return new BitmapDrawable(getApplicationContext().getResources(),decodedByte);
    }
}
