package com.diadema.catalogproducts.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.diadema.catalogproducts.EstructuraScrollingActivity;
import com.diadema.catalogproducts.FasesProducto;
import com.diadema.catalogproducts.R;
import com.diadema.catalogproducts.entities.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CEK on 22/03/2019.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private List<Producto> data;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Producto> data) {
        this.context = context;
        this.data = data;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView mTextView;
        ImageView imageView;
        Button mFases;
        Button mEstructura;

        MyViewHolder(View v){
            super(v);
            mCardView = v.findViewById(R.id.materialCard);
            mTextView = v.findViewById(R.id.sku);
            imageView = v.findViewById(R.id.componente);
            mFases = v.findViewById(R.id.btn_fases);
            mEstructura = v.findViewById(R.id.btn_estructura);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_item_producto, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position){
        Log.d(TAG, "onBindViewHolder: called.");
        Producto all = data.get(position);
        String sku = context.getString(R.string.concat_sku)+" "+all.getDefault_code();
        holder.mTextView.setText(sku);

        if (all.getImage().isEmpty()){
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sin_imagen));
        }else {
            Drawable imagen = base64ToDrawable(all.getImage());
            holder.imageView.setImageDrawable(imagen);
        }

        /*holder.itemView.setOnClickListener(v -> {
            int position1 = holder.getAdapterPosition();
            Toast.makeText(context, position1 +"", Toast.LENGTH_SHORT).show();
            final Intent intent;
            intent =  new Intent(context, FasesProducto.class);
            context.startActivity(intent);
        });*/

        holder.mFases.setOnClickListener(v ->{
            int position1 = holder.getAdapterPosition();
            //Toast.makeText(context, position1 +" "+ all.getProduct_tmpl_id()[0], Toast.LENGTH_SHORT).show();
            final Intent intent;
            intent =  new Intent(context, FasesProducto.class);
            intent.putExtra("productId", all.getId());
            context.startActivity(intent);
        });

        holder.mEstructura.setOnClickListener(v ->{
            int position1 = holder.getAdapterPosition();
            //Toast.makeText(context, position1 +"", Toast.LENGTH_SHORT).show();
            final Intent intent;
            intent =  new Intent(context, EstructuraScrollingActivity.class);
            intent.putExtra("imagen", all.getImage());
            intent.putExtra("sku", sku);
            intent.putExtra("product_tmpl_id", all.getProduct_tmpl_id()[0]);
            context.startActivity(intent);
        });
    }

    private Drawable base64ToDrawable(String imagen_mediun){
        byte[] decodedString = Base64.decode(imagen_mediun, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return new BitmapDrawable(context.getResources(),decodedByte);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public void setFilter(ArrayList<Producto> dataAdd){
        this.data = new ArrayList<>();
        this.data.addAll(dataAdd);
        notifyDataSetChanged();
    }
}