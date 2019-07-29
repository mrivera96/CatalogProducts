package com.diadema.catalogproducts.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.diadema.catalogproducts.R;
import com.diadema.catalogproducts.entities.Estructura;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sergio Ayestas on 10/04/2019.
 */

public class RecyclerViewAdapterEstructura extends RecyclerView.Adapter<RecyclerViewAdapterEstructura.MyViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private List<Estructura> data ;
    private Context context;

    public RecyclerViewAdapterEstructura(Context context, ArrayList<Estructura> data) {
        this.context = context;
        this.data = data;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewTitle;
        TextView mTextViewDescrip;

        MyViewHolder(View v){
            super(v);
            mTextViewTitle = v.findViewById(R.id.codigoEstructura);
            mTextViewDescrip = v.findViewById(R.id.descripcionEstructura);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapterEstructura.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.desing_item_estructura, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterEstructura.MyViewHolder holder, int position){
        Log.d(TAG, "onBindViewHolder: called.");
        Estructura all = data.get(position);
        holder.mTextViewTitle.setText(all.getProduct_id()[0]);
        holder.mTextViewDescrip.setText(all.getProduct_id()[1]);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
