package com.rotciv.marvelstore.api;

import com.rotciv.marvelstore.model.Catalog;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {

    @GET ("comics?apikey=04284545f0c36cce84514e7226aaea2d&ts=1&hash=249a1a90a1cba8b59ab474e0d3fc91f8")
    Call<Catalog> callCatalog();
}
