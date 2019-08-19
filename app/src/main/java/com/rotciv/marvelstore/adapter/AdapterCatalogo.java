package com.rotciv.marvelstore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rotciv.marvelstore.R;
import com.rotciv.marvelstore.model.Result;

import java.util.ArrayList;
import java.util.List;

public class AdapterCatalogo extends RecyclerView.Adapter<AdapterCatalogo.MyViewHolder> {

    Context context;
    private List<Result> comics;

    public AdapterCatalogo(Context context, List<Result> comics) {
        this.context = context;
        this.comics = comics;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_catalogo, viewGroup, false   );

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Result comic = comics.get(i);

        String url = comic.getThumbnail().getPath().replace("http://", "https://");

        myViewHolder.comicTitulo.setText(comic.getTitle());

        //Para evitar que itens que não são raros fiquem vermelhos, por causa da implementação do RecyclerView
        myViewHolder.comicTitulo.setTextColor(Color.BLACK);
        if (comic.getRaridade() == 1) {
            myViewHolder.comicTitulo.setTextColor(Color.RED);
        }
        myViewHolder.comicPreco.setText("$" + comic.getPrices().get(0).getPrice());
        Glide.with(context).load(url + "/detail.jpg").into(myViewHolder.imagemComic);
    }


    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView comicTitulo, comicPreco;
        ImageView imagemComic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comicTitulo = itemView.findViewById(R.id.textTitulo);
            comicPreco = itemView.findViewById(R.id.textPreco);
            imagemComic = itemView.findViewById(R.id.imagemComic);
        }
    }
}
