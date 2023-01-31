package com.example.pokedexapp.entitys;

import java.io.Serializable;

public class EvolutionChainUrl implements Serializable {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "EvolutionChain{" +
                "url='" + url + '\'' +
                '}';
    }
}
