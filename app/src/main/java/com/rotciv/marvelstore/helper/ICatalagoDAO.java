package com.rotciv.marvelstore.helper;

import com.rotciv.marvelstore.model.Result;

import java.util.List;

public interface ICatalagoDAO {

    public boolean salvar(List<Result> comics);
    public List<Result> listar ();
}
