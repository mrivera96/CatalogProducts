package com.diadema.catalogproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.diadema.catalogproducts.Adapters.MyPagerFasesAdapter;
import com.diadema.catalogproducts.entities.Fases;
import com.diadema.catalogproducts.entities.FasesResponse;
import com.diadema.catalogproducts.services.ApiService;
import com.diadema.catalogproducts.services.NetworkStatusManager;
import com.diadema.catalogproducts.services.RetrofitBuilder;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by César Andrade on 11/04/2019.
 */

public class FasesProducto extends AppCompatActivity {
    private static final String TAG = "FasesProducto";
    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private int[]layouts;
    private Button btnSkip;
    private Button btnNext;
    private MyPagerFasesAdapter pagerAdapter;
    String productId;
    protected static ArrayList<Fases> listaFases;
    Call<FasesResponse> callFases;
    ApiService service;
    private ProgressBar progressBarFase;
    private ConstraintLayout contenedorVPPAdreFase, contenedorRecycleListFase;
    private LinearLayout contenedorMensajeFase;
    private ImageView imagenMensaje;
    private TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.desing_fase_producto);
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            productId = parametros.getString("productId");
        }

        progressBarFase = findViewById(R.id.progressBarFase);
        contenedorVPPAdreFase = findViewById(R.id.contenedorVPPAdreFase);
        contenedorMensajeFase = findViewById(R.id.contenedorMensajeFase);
        contenedorRecycleListFase = findViewById(R.id.contenedorRecycleListFase);
        imagenMensaje = findViewById(R.id.imagenMensajeEstructura);
        tvMensaje = findViewById(R.id.tvMensajeEstructura);

        viewPager = findViewById(R.id.view_pager);
        layoutDot = findViewById(R.id.dotLayout);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        //When user press skip, start Main Activity
        btnSkip.setOnClickListener(view -> finish());

        btnNext.setOnClickListener(view -> {
            int currentPage = viewPager.getCurrentItem()+1;
            if(currentPage < layouts.length) {
                //move to next page
                viewPager.setCurrentItem(currentPage);
            } else {
                finish();
            }
        });

        switch (NetworkStatusManager.status(Objects.requireNonNull(getApplicationContext()))) {
            case "Offline":
                showMensaje(getResources().getDrawable(R.drawable.no_wifi_blanco), getString(R.string.sin_internet));
                break;
            case "Mobile data":
                showMensaje(getResources().getDrawable(R.drawable.no_wifi_blanco), getString(R.string.no_wifi));
                break;
            case "SSID Incorrect":
                showMensaje(getResources().getDrawable(R.drawable.no_wifi_blanco), getString(R.string.no_coorporativa));
                break;
            case "SSID Correct":
                getListFases();
                break;
        }
    }

    private void setDotStatus(int page){
        layoutDot.removeAllViews();
        TextView[] dotstv = new TextView[layouts.length];
        for (int i = 0; i < dotstv.length; i++) {
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);
        }
        //Set current dot active
        if(dotstv.length>0){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void getListFases(){
        showLoading();
        listaFases = new ArrayList<>();
        callFases = service.fases(productId);
        //Toast.makeText(getApplicationContext(),"Inicio getListProducts CanadaFragment", Toast.LENGTH_SHORT).show();
        callFases.enqueue(new Callback<FasesResponse>() {
            @Override
            public void onResponse(@NonNull Call<FasesResponse> call, @NonNull Response<FasesResponse> response) {
                Log.w(TAG, "onResponse: " + response );
                //Toast.makeText(getApplicationContext(),"Hubo respuesta getListProducts CanadaFragment", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    assert response.body() != null;
                    int size = response.body().getData().size();
                    if( size == 0 ){
                        showMensaje(getResources().getDrawable(R.drawable.sin_productos_blanca), getString(R.string.sin_productos));
                    }else {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            listaFases.add(get(response.body().getData().get(i).getId(),
                                    response.body().getData().get(i).getName(),
                                    response.body().getData().get(i).getNumero_fase(),
                                    response.body().getData().get(i).getImg_fase1()));
                        }

                        layouts = new int[size];
                        for (int i = 0; i < size; i++) {
                            layouts[i] = R.layout.desing_item_fase_producto;
                        }

                        setDotStatus(0);
                        pagerAdapter = new MyPagerFasesAdapter(layouts, listaFases,getApplicationContext());
                        viewPager.setAdapter(pagerAdapter);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                            @Override
                            public void onPageSelected(int position) {
                                if(position == layouts.length-1){
                                    //LAST PAGE
                                    btnNext.setText(getResources().getString(R.string.finalVistas));
                                    btnSkip.setVisibility(View.GONE);
                                }else {
                                    btnNext.setText(getResources().getString(R.string.siguiente));
                                    btnSkip.setVisibility(View.VISIBLE);
                                }
                                setDotStatus(position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {}
                        });
                        showRecycleList();
                    }
                }else {
                    //handleErrors(response.errorBody());
                    assert response.errorBody() != null;
                    try {
                        Log.w(TAG, "onError: " + response.errorBody().string());
                        switch (response.message()){
                            case "Internal Server Error":
                                showMensaje(getResources().getDrawable(R.drawable.sin_respuesta_blanco), getString(R.string.error_500));
                                break;
                            default:
                                showMensaje(getResources().getDrawable(R.drawable.sin_respuesta_blanco), getString(R.string.sin_respuesta)+"Error");
                                break;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FasesResponse> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                    String failure = t.getMessage();
                    if (failure.contains("Internal Server Error")){
                        showMensaje(getResources().getDrawable(R.drawable.sin_respuesta_blanco), getString(R.string.error_500));
                    }

                    if (failure.contains("Expected BEGIN_ARRAY")){
                        showMensaje(getResources().getDrawable(R.drawable.sin_respuesta_blanco),getString(R.string.deformed_data));
                    }else
                        showMensaje(getResources().getDrawable(R.drawable.sin_respuesta_blanco), getString(R.string.sin_respuesta));
                } catch (Exception e){
                    e.printStackTrace();
                    getListFases();
                }
            }
        });
    }

    private Fases get(String id, String name, String numero_fase, String img_fase1){
        return new Fases( id, name, numero_fase, img_fase1);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(contenedorVPPAdreFase);
        contenedorRecycleListFase.setVisibility(View.GONE);
        progressBarFase.setVisibility(View.VISIBLE);
    }

    private void showRecycleList(){
        TransitionManager.beginDelayedTransition(contenedorVPPAdreFase);
        progressBarFase.setVisibility(View.GONE);
        contenedorMensajeFase.setVisibility(View.GONE);
        contenedorRecycleListFase.setVisibility(View.VISIBLE);
    }

    private void showMensaje(Drawable imagMensaje, String mensaje){
        imagenMensaje.setImageDrawable(imagMensaje);
        tvMensaje.setText(mensaje);
        TransitionManager.beginDelayedTransition(contenedorVPPAdreFase);
        btnNext.setVisibility(View.GONE);
        contenedorRecycleListFase.setVisibility(View.VISIBLE);
        contenedorMensajeFase.setVisibility(View.VISIBLE);
        progressBarFase.setVisibility(View.GONE);
    }

    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
