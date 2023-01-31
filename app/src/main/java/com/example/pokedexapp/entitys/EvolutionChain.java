package com.example.pokedexapp.entitys;

import java.io.Serializable;
import java.util.List;

public class EvolutionChain implements Serializable {

    private List<EvolutionChain> evolves_to;

    private Species species;

    public List<EvolutionChain> getEvolves_to() {
        return evolves_to;
    }

    public void setEvolves_to(List<EvolutionChain> evolves_to) {
        this.evolves_to = evolves_to;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "EvolutionChain{" +
                "evolves_to=" + evolves_to +
                ", species=" + species +
                '}';
    }
}
