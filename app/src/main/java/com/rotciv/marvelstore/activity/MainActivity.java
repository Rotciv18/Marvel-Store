package com.rotciv.marvelstore.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rotciv.marvelstore.R;
import com.rotciv.marvelstore.adapter.AdapterCatalogo;
import com.rotciv.marvelstore.api.DataService;
import com.rotciv.marvelstore.helper.CatalagoDAO;
import com.rotciv.marvelstore.helper.DbHelper;
import com.rotciv.marvelstore.helper.RecyclerItemClickListener;
import com.rotciv.marvelstore.model.Catalog;
import com.rotciv.marvelstore.model.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String baseUrl = "https://gateway.marvel.com/v1/public/";
    Retrofit retrofit;
    Catalog catalogo = new Catalog();
    List<Result> comics = new ArrayList<>();
    RecyclerView recyclerView;
    CatalagoDAO catalagoDAO;
    DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catalagoDAO = new CatalagoDAO(getApplicationContext());
        db = new DbHelper(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);

        if (!existeTabela()) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            recuperarCatalogo();
        } else {
            comics = catalagoDAO.listar();
            iniciaRecyclerView();
        }
    }

    public void recuperarCatalogo() {
        DataService service = retrofit.create(DataService.class);
        Call<Catalog> call = service.callCatalog();

        call.enqueue(new Callback<Catalog>() {
            @Override
            public void onResponse(Call<Catalog> call, Response<Catalog> response) {
                if (response.isSuccessful()) {
                    catalogo = response.body();
                    comics = catalogo.getData().getResults();

                    sorteiaRaridade();
                    iniciaRecyclerView();
                    salvarDB();
                }
            }

            @Override
            public void onFailure(Call<Catalog> call, Throwable t) {
                Log.e("Erro", t.getMessage());
            }
        });
    }

    public void iniciaRecyclerView() {
        //Configura adapter

        AdapterCatalogo adapter = new AdapterCatalogo(MainActivity.this, comics);

        //configura RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        for (int i = 0; comics.size() > i; i++) {
            Log.i(comics.get(i).getTitle() + ": ", comics.get(i).getRaridade().toString());
        }
        addClickEvent();
    }

    //Evento de click no RecyclerView
    public void addClickEvent() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Result comic = comics.get(position);
                detalharComic(comic);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));
    }

    public void detalharComic(Result comic) {
        Intent intent = new Intent(getApplicationContext(), DetalhesActivity.class);

        String url = comic.getThumbnail().getPath().replace("http://", "https://");
        intent.putExtra("url", url);

        intent.putExtra("title", comic.getTitle());

        intent.putExtra("raridade", comic.getRaridade());

        intent.putExtra("price", comic.getPrices().get(0).getPrice());

        intent.putExtra("id", comic.getId());

        startActivity(intent);
    }

    public void salvarDB() {
        catalagoDAO.salvar(comics);
    }

    public boolean existeTabela() {
        SQLiteDatabase read = db.getReadableDatabase();
        String sql = "SELECT * FROM " + DbHelper.TABELA_CATALOGO + " ;";
        Cursor c = read.rawQuery(sql, null);

        if (!c.moveToNext())
            return false;
        return true;
    }

    public void irCarrinhoActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), CarrinhoActivity.class);
        startActivity(intent);
    }

    public void sorteiaRaridade() {
        Integer n = (int) (comics.size() * 0.12);
        Random rand = new Random();
        Integer index;
        //Random rand = new Random();

        Log.i("Iniciando", "De 0 a" + comics.size());
        for (; n > 0; n--) {
            index = rand.nextInt(comics.size());
            while (comics.get(index).getRaridade() == 1) {
                index = rand.nextInt(comics.size());
                Log.i("Valor encontrado:", index.toString());
            }
            comics.get(index).setRaridade(1);
        }
    }
}
