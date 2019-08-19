package com.rotciv.marvelstore.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.rotciv.marvelstore.R;
import com.rotciv.marvelstore.adapter.AdapterCarrinho;
import com.rotciv.marvelstore.adapter.AdapterCatalogo;
import com.rotciv.marvelstore.helper.CarrinhoDAO;
import com.rotciv.marvelstore.helper.DbHelper;
import com.rotciv.marvelstore.helper.RecyclerItemClickListener;
import com.rotciv.marvelstore.model.Result;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Result> comics;
    CarrinhoDAO carrinhoDAO;
    Double valorTotal;

    Button botaoComprar;
    EditText textCupom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        getSupportActionBar().hide();

        //Configurações iniciais
        carrinhoDAO = new CarrinhoDAO(getApplicationContext());
        botaoComprar = findViewById(R.id.botaoComprar);
        textCupom = findViewById(R.id.textCupom);
        recyclerView = findViewById(R.id.recyclerView2);

        //recupera todos os itens da tabela 'carrinho'
        recuperaItens();

        //lista os itens
        iniciaRecyclerView();

        calculaValorTotal(":)");

        //Põe um Listener no campo de texto. O preço deve atualizar se
        //o campo for preenchido de forma adequada
            //cupomcomum para cupons comuns
            //cupomraro para cupons raros
        cupomListener();

    }

    public void recuperaItens() {
        CarrinhoDAO carrinhoDAO = new CarrinhoDAO(getApplicationContext());
        comics = carrinhoDAO.listar();
    }

    public void iniciaRecyclerView() {
        //Configura adapter

        AdapterCarrinho adapter = new AdapterCarrinho(CarrinhoActivity.this, comics);

        //configura RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        addClickEvent();
    }

    //Evento de click no RecyclerView
    public void addClickEvent() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CarrinhoActivity.this);

                //Configura Título e mensagem
                dialog.setTitle("Cancelar um item");
                dialog.setMessage("Deseja cancelar a compra do item '" + comics.get(position).getTitle() + "'?");
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deletarItemCarrinho(position);

                    }
                });
                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Faz nada
                    }
                });
                dialog.create();
                dialog.show();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //faz nada
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));
    }

    public void deletarItemCarrinho(int position) {
        Result comic = comics.get(position);
        comics.remove(position);
        carrinhoDAO.deletar(comic);

        recuperaItens();
        iniciaRecyclerView();
        calculaValorTotal("Oieeeeeee");
    }

    public void calculaValorTotal(String cupom) {
        valorTotal = 0.0;
        Double desconto = 1.0;
        if (cupom.equals("comum"))
            desconto = 0.9;

        if (cupom.equals("raro")) {
            desconto = 0.75;

            for (int i = 0; i < comics.size(); i++) {
                Double valor = Double.parseDouble(comics.get(i).getPrices().get(0).getPrice());
                valorTotal = valorTotal + valor * desconto;
            }
        } else {
            //Cupons comuns irão descontar apenas comics comuns
            for (int i = 0; i < comics.size(); i++) {
                Double valor = Double.parseDouble(comics.get(i).getPrices().get(0).getPrice());
                if (comics.get(i).getRaridade() == 1)
                    valorTotal = valorTotal + valor;
                else
                    valorTotal = valorTotal + valor * desconto;
            }
        }
        botaoComprar.setText("Comprar por " + String.format("%.2f", valorTotal));
    }

    public void cupomListener () {
        textCupom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //Cupom será validado no momento em que for digitado
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textCupom.getText().toString().equals("cupomraro")) {
                    Toast.makeText(CarrinhoActivity.this, "Cupom válido!", Toast.LENGTH_SHORT).show();
                    calculaValorTotal("raro");
                } else if (textCupom.getText().toString().equals("cupomcomum")) {
                    Toast.makeText(CarrinhoActivity.this, "Cupom válido!", Toast.LENGTH_SHORT).show();
                    calculaValorTotal("comum");
                } else
                    calculaValorTotal("gerenteficoulouco");
            }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
