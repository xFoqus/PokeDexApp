package com.example.pokedexapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pokedexapp.adapter.AdaptadorListaPokemon;
import com.example.pokedexapp.adapter.RecyclerItemClickListener;
import com.example.pokedexapp.conexionPokeAPI.ServicioPokeAPI;
import com.example.pokedexapp.entitys.Chain;
import com.example.pokedexapp.entitys.ChainObject;
import com.example.pokedexapp.entitys.EvolutionChain;
import com.example.pokedexapp.entitys.ListaPokemonAPI;
import com.example.pokedexapp.entitys.Pokemon;
import com.example.pokedexapp.entitys.PokemonSpecies;
import com.example.pokedexapp.entitys.Type;
import com.example.pokedexapp.entitys.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit conexionRetrofit;

    Context contexto;

    private int offset;

    private RecyclerView listaPokemon;
    private AdaptadorListaPokemon adaptadorListaPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contexto = this;

        listaPokemon = findViewById(R.id.listaPokemon);
        adaptadorListaPokemon = new AdaptadorListaPokemon(contexto);
        listaPokemon.setAdapter(adaptadorListaPokemon);
        final GridLayoutManager layoutManager = new GridLayoutManager(contexto,2);
        listaPokemon.setLayoutManager(layoutManager);


        listaPokemon.addOnItemTouchListener(new RecyclerItemClickListener(this, listaPokemon, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posicion) {
                itemSeleccionadoClick(v,posicion);
            }

            @Override
            public void onLongItemClick(View v, int posicion) {

            }
        }));

        listaPokemon.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0){
                    int visibles = layoutManager.getChildCount();
                    int total = layoutManager.getItemCount();
                    int pasado = layoutManager.findFirstVisibleItemPosition();

                    if ((visibles + pasado) >= total){
                        offset += 12;
                        obtenerDatos(offset);
                    }
                }
            }
        });

        conexionRetrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        offset = 0;
        obtenerDatos(0);
    }


    private void itemSeleccionadoClick(View v, int posicion){
        Intent intent = new Intent(v.getContext(),DetallesPokemon.class);
        mandaTiposPorId(adaptadorListaPokemon.getDatosPokemon().get(posicion), intent);
    }

    private void mandaTiposPorId(Pokemon p, Intent intent){
        ServicioPokeAPI servicioPokeAPI = conexionRetrofit.create(ServicioPokeAPI.class);
        Call<Pokemon> poke = servicioPokeAPI.pokemonPorID(p.getId());
        poke.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(response.isSuccessful()){
                    Pokemon pokemon = response.body();
                    List<Types> types = pokemon.getTypes();
                    int weight = pokemon.getWeight();
                    p.setTypes(types);
                    p.setWeight(weight);
                    intent.putExtra("pokemon",p);
                    startActivity(intent);

                }else{
                    Toast.makeText(contexto,"Conexion fallida: " + response.toString(),Toast.LENGTH_LONG).show();
                    Log.e("aplicación","Respuestita: " + response.toString());
                }
            }
            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                System.out.println("ERROR GET TIPOS");
            }
        });

    }

    private void obtenerDatos(int offset) {
        ServicioPokeAPI servicio = conexionRetrofit.create(ServicioPokeAPI.class);
        Call<ListaPokemonAPI> pokemonAPICall = servicio.hazteConTodos(12,offset);

        pokemonAPICall.enqueue(new Callback<ListaPokemonAPI>() {
            @Override
            public void onResponse(Call<ListaPokemonAPI> call, Response<ListaPokemonAPI> response) {
                if (response.isSuccessful()){
                    Log.e("aplicación","Respuestita: " + response.body());

                    ListaPokemonAPI listaPokemonAPI = response.body();
                    ArrayList<Pokemon> listaPrueba = listaPokemonAPI.getResults();
                    adaptadorListaPokemon.agregarListaPokemon(listaPrueba);

                }else{
                    Toast.makeText(contexto,"Conexion fallida: " + response.toString(),Toast.LENGTH_LONG).show();
                    Log.e("aplicación","Respuestita: " + response.toString());
                }

            }

            @Override
            public void onFailure(Call<ListaPokemonAPI> call, Throwable t) {
                Log.e("aplicación","Fallito: " + t.getMessage());
            }
        });
    }
}