package com.diadema.catalogproducts.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.diadema.catalogproducts.R;
import com.diadema.catalogproducts.entities.Fases;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by CEK on 28/03/2019.
 */

public class MyPagerFasesAdapter extends PagerAdapter {
    private int[]layouts;
    private Context context;
    private List<Fases> data;

    public MyPagerFasesAdapter(int[] layouts, List<Fases> data, Context context) {
        this.layouts = layouts;
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layouts[position], container, false);
        TextView title = v.findViewById(R.id.tv_Title);
        Fases all = data.get(position);
        String numFase = "Fase " + all.getNumero_fase();
        title.setText(numFase);

        TextView description = v.findViewById(R.id.tv_Description);
        description.setText(all.getName());

        ImageView imagenFase = v.findViewById(R.id.iv_Component);
        if (all.getImg_fase1().isEmpty()){
            imagenFase.setImageDrawable(context.getResources().getDrawable(R.drawable.sin_imagen));
        }else {
            Drawable imagen = base64ToDrawable(all.getImg_fase1());
            imagenFase.setImageDrawable(imagen);
        }

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View v = (View)object;
        container.removeView(v);
    }

    private Drawable base64ToDrawable(String imagen_mediun){
        byte[] decodedString = Base64.decode(imagen_mediun, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return new BitmapDrawable(context.getResources(),decodedByte);
    }
}
