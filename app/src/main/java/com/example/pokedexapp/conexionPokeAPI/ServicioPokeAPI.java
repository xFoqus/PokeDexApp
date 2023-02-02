package com.example.pokedexapp.conexionPokeAPI;

import com.example.pokedexapp.entitys.Chain;
import com.example.pokedexapp.entitys.ChainObject;
import com.example.pokedexapp.entitys.EvolutionChain;
import com.example.pokedexapp.entitys.ListaPokemonAPI;
import com.example.pokedexapp.entitys.Pokemon;
import com.example.pokedexapp.entitys.PokemonSpecies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServicioPokeAPI {
    @GET("pokemon")
    Call<ListaPokemonAPI> hazteConTodos(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{id}")
    Call<Pokemon> pokemonPorID(@Path("id") int id);

    @GET("pokemon-species/{name}")
    Call<PokemonSpecies>getPokemonSpeciesByName(@Path("name") String name);

    @GET("evolution-chain/{id}")
    Call<ChainObject> getEvolutionChainByChainID(@Path("id")int id);

}
