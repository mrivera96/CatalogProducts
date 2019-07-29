package com.diadema.catalogproducts;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.diadema.catalogproducts.Adapters.RecyclerViewAdapter;
import com.diadema.catalogproducts.entities.Producto;
import com.diadema.catalogproducts.entities.ProductoResponse;
import com.diadema.catalogproducts.services.ApiService;
import com.diadema.catalogproducts.services.NetworkStatusManager;
import com.diadema.catalogproducts.services.RetrofitBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by César Andrade on 26/03/2019.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EstadosUnidosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EstadosUnidosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstadosUnidosFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "EstadosUnidosFragment";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private static ArrayList<Producto> listaProductosEU;
    private Call<ProductoResponse> callProductos;
    private ApiService service;
    private ProgressBar progressBarUSA;
    private ConstraintLayout contenedorPadreUSA;
    private LinearLayout contenedorMensajeUSA, contenedorRecycleListUSA;
    private ImageView imagenMensaje;
    private TextView tvMensaje;
    private OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPerfil.
     */
    // TODO: Rename and change types and number of parameters
    private static EstadosUnidosFragment newInstance(String param1, String param2) {
        EstadosUnidosFragment fragment = new EstadosUnidosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usa, container, false);
        iniComponent(view);

        switch (NetworkStatusManager.status(Objects.requireNonNull(getContext()))) {
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
                try {
                    getListProducts();
                }catch (Exception e){
                    Toast.makeText(getContext(), getContext().getString(R.string.no_te_emociones),Toast.LENGTH_LONG).show();
                }
                break;
        }

        return view;
    }

    private void iniComponent(View view){
        recyclerView = view.findViewById(R.id.recycle_list_usa);
        progressBarUSA = view.findViewById(R.id.progressBarUSA);
        contenedorPadreUSA = view.findViewById(R.id.contenedorPadreUSA);
        contenedorMensajeUSA = view.findViewById(R.id.contenedorMensajeUSA);
        contenedorRecycleListUSA = view.findViewById(R.id.contenedorRecycleListUSA);
        imagenMensaje = view.findViewById(R.id.imagenMensajeUSA);
        tvMensaje = view.findViewById(R.id.tvMensajeUSA);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.buscador, menu);
        super.onCreateOptionsMenu(menu,inflater);
        MenuItem item = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        item.setOnActionExpandListener( new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                adapter.setFilter(listaProductosEU);
                return true;
            }
        });
    }

    private void getListProducts(){
        showLoading();
        listaProductosEU = new ArrayList<>();
        callProductos = service.productos(getString(R.string.region_eeuu));
        callProductos.enqueue(new Callback<ProductoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductoResponse> call, @NonNull Response<ProductoResponse> response) {
                Log.w(TAG, "onResponse: " + response);
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getData().size() == 0 ){
                        showMensaje(getResources().getDrawable(R.drawable.sin_productos), getString(R.string.sin_productos));
                    }else {
                        showRecycleList();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            listaProductosEU.add(get(response.body().getData().get(i).getDefault_code(),
                                    response.body().getData().get(i).getProduct_tmpl_id(),
                                    response.body().getData().get(i).getImage(),
                                    response.body().getData().get(i).getName(),
                                    response.body().getData().get(i).getId()));
                        }

                        initRecycleList(listaProductosEU);
                    }
                }else {
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
            public void onFailure(@NonNull Call<ProductoResponse> call, @NonNull Throwable t) {
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
                    e.printStackTrace();
                }
            }
        });
    }

    private Producto get(String default_code, String[] product_tmpl_id, String image, String name, String id){
        return new Producto(default_code, product_tmpl_id, image, name, id);
    }

    private void initRecycleList(List<Producto> listaProductosEU){
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_left_to_right);
        adapter = new RecyclerViewAdapter(getContext(), listaProductosEU);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutAnimation(controller);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callProductos != null) {
            callProductos.cancel();
            callProductos = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            try {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                populateViewForOrientation(inflater, (ViewGroup) Objects.requireNonNull(getView()));
                if (!listaProductosEU.isEmpty()){
                    initRecycleList(listaProductosEU);
                }else
                    getListProducts();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_canada, viewGroup);
        iniComponent(subview);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(contenedorPadreUSA);
        contenedorRecycleListUSA.setVisibility(View.VISIBLE);
        contenedorMensajeUSA.setVisibility(View.GONE);
        progressBarUSA.setVisibility(View.VISIBLE);
    }

    private void showRecycleList(){
        TransitionManager.beginDelayedTransition(contenedorPadreUSA);
        progressBarUSA.setVisibility(View.GONE);
    }

    private void showMensaje(Drawable imagMensaje, String mensaje){
        imagenMensaje.setImageDrawable(imagMensaje);
        tvMensaje.setText(mensaje);
        TransitionManager.beginDelayedTransition(contenedorPadreUSA);
        contenedorMensajeUSA.setVisibility(View.VISIBLE);
        contenedorRecycleListUSA.setVisibility(View.GONE);
        progressBarUSA.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try{
            ArrayList<Producto> nuevalista = filter(listaProductosEU, s);
            adapter.setFilter(nuevalista);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
    private ArrayList<Producto> filter(ArrayList<Producto> data, String buscar ){
        ArrayList<Producto> filtrado = new ArrayList<>();
        try{
            buscar = buscar.toLowerCase();
            for (Producto encuentra: data){
                String valor = encuentra.getDefault_code().toLowerCase();
                if (valor.contains(buscar)){
                    filtrado.add(encuentra);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return filtrado;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}