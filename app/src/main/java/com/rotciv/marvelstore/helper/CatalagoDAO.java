package com.rotciv.marvelstore.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.rotciv.marvelstore.model.Price;
import com.rotciv.marvelstore.model.Result;
import com.rotciv.marvelstore.model.Thumbnail;

import java.util.ArrayList;
import java.util.List;

public class CatalagoDAO implements ICatalagoDAO{

    private SQLiteDatabase write;
    private SQLiteDatabase read;
    public Context context;

    public CatalagoDAO(Context context) {
        DbHelper db = new DbHelper(context);
        write = db.getWritableDatabase();
        read = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(List<Result> comics) {

        for (int i = 0; i < comics.size(); i++) {
            Result comic = comics.get(i);

            ContentValues cv = new ContentValues();
            cv.put("id", comic.getId());
            cv.put("title", comic.getTitle());
            cv.put("price", comic.getPrices().get(0).getPrice());
            cv.put("raridade", comic.getRaridade());
            cv.put("url", comic.getThumbnail().getPath());

            try {
                write.insert(DbHelper.TABELA_CATALOGO, null, cv);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Result> listar() {
        List<Result> comics = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_CATALOGO + " ;";
        Cursor c = read.rawQuery(sql, null);

        while (c.moveToNext()){
            Result comic = new Result();

            //Inicializações para evitar acessos a void
            comic.setThumbnail(new Thumbnail());
            comic.setPrices(new ArrayList<Price>());
            comic.getPrices().add(new Price());

            Integer id = c.getInt(c.getColumnIndex("id"));
            Integer raridade = c.getInt(c.getColumnIndex("raridade"));
            Double price = c.getDouble(c.getColumnIndex("price"));
            String title = c.getString(c.getColumnIndex("title"));
            String url = c.getString(c.getColumnIndex("url"));

            comic.setId(id.toString());
            comic.getThumbnail().setPath(url);
            comic.getPrices().get(0).setPrice(price.toString());
            comic.setTitle(title);
            if (raridade == 0)
                comic.setRaridade(0);
            else
                comic.setRaridade(1);


            comics.add(comic);
        }

        return comics;
    }
}
