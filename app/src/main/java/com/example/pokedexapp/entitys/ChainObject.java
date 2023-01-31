package com.example.pokedexapp.entitys;

import java.io.Serializable;

public class ChainObject implements Serializable {
    private Chain chain;

    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    @Override
    public String toString() {
        return "ChainObject{" +
                "chain=" + chain +
                '}';
    }
}
