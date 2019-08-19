package com.rotciv.marvelstore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rotciv.marvelstore.R;
import com.rotciv.marvelstore.model.Result;

import java.util.List;

public class AdapterCarrinho extends RecyclerView.Adapter<AdapterCarrinho.MyViewHolder> {

    Context context;
    List<Result> comics;

    public AdapterCarrinho(Context context, List<Result> comics) {
        this.context = context;
        this.comics = comics;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_carrinho, viewGroup, false   );

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Result comic = comics.get(i);

        myViewHolder.title.setText(comic.getTitle());


        if (comic.getRaridade() == 1)
            myViewHolder.title.setTextColor(Color.RED);
        else
            myViewHolder.title.setTextColor(Color.BLACK);
        myViewHolder.preco.setText("$" + comic.getPrices().get(0).getPrice());
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, preco;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textTitle);
            preco = itemView.findViewById(R.id.textPrice);
        }
    }
}
