package com.rotciv.marvelstore.helper;

import com.rotciv.marvelstore.model.Result;

import java.util.List;

public interface ICarrinhoDAO {

    public boolean salvar (Result comic);
    public boolean deletar (Result comic);
    public List<Result> listar ();
}
