package com.example.pokedexapp.entitys;

import java.io.Serializable;

public class PokemonSpecies implements Serializable {
    private EvolutionChainUrl evolution_chain;

    public EvolutionChainUrl getEvolution_chain() {
        return evolution_chain;
    }

    public void setEvolution_chain(EvolutionChainUrl evolution_chain) {
        this.evolution_chain = evolution_chain;
    }

    @Override
    public String toString() {
        return "PokemonSpecies{" +
                "evolution_chain=" + evolution_chain +
                '}';
    }
}
