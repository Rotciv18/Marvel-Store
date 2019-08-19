package com.rotciv.marvelstore.model;

import android.os.Parcelable;

import java.util.List;

public class Result {

    private String id;
    private String title;
    private Thumbnail thumbnail;
    private List<Price> prices;
    private Integer raridade = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public Integer getRaridade() {
        return raridade;
    }

    public void setRaridade(Integer raridade) {
        this.raridade = raridade;
    }
}
