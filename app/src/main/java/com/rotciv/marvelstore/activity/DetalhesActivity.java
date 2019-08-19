package com.rotciv.marvelstore.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rotciv.marvelstore.R;
import com.rotciv.marvelstore.helper.CarrinhoDAO;
import com.rotciv.marvelstore.helper.CatalagoDAO;
import com.rotciv.marvelstore.model.Price;
import com.rotciv.marvelstore.model.Result;

import java.util.ArrayList;

public class DetalhesActivity extends AppCompatActivity {

    ImageView comicImagem;
    TextView textTitle, textRaridade, textPreco, textDescricao;
    Result comic = new Result();

    String id, title, url, preco, description;
    Integer raridade;

    CarrinhoDAO carrinhoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        getSupportActionBar().hide();

        carrinhoDAO = new CarrinhoDAO(getApplicationContext());

        comicImagem = findViewById(R.id.comicImagem);
        textPreco = findViewById(R.id.textPreco);
        textRaridade = findViewById(R.id.textRaridade);
        textTitle = findViewById(R.id.textTitle);
        textDescricao = findViewById(R.id.textDescricao);

        recuperarDados();
    }

    public void recuperarDados(){
        Bundle dados = getIntent().getExtras();
        id = (String) dados.getSerializable("id");
        url = (String) dados.getSerializable("url");
        title = (String) dados.getSerializable("title");
        preco = (String) dados.getSerializable("price");
        raridade = (Integer) dados.getSerializable("raridade");
        description = (String) dados.getSerializable("description");


        //Atualiza views com dados passados da intent
        Glide.with(DetalhesActivity.this).load(url + "/detail.jpg").into(comicImagem);
        textTitle.setText(title);
        textDescricao.setText(description);
        textPreco.setText("$" + preco);

        if (raridade == 0){
            textRaridade.setText("Comum");
        } else {
            textRaridade.setText("Raro");
            textRaridade.setTextColor(Color.RED);
        }
    }

    public void adicionarPedido(View view){
        comic.setPrices(new ArrayList<Price>());
        comic.getPrices().add(new Price());

        comic.setId(id);
        comic.setTitle(title);
        comic.getPrices().get(0).setPrice(preco);
        comic.setRaridade(raridade);

        carrinhoDAO.salvar(comic);

        Toast.makeText(this, "Seu carrinho foi atualizado!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
