package com.example.pokedexapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedexapp.conexionPokeAPI.ServicioPokeAPI;
import com.example.pokedexapp.entitys.ChainObject;
import com.example.pokedexapp.entitys.Pokemon;
import com.example.pokedexapp.entitys.PokemonSpecies;
import com.example.pokedexapp.entitys.Types;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallesPokemon extends AppCompatActivity {

    private Pokemon pokemon;

    private String type1 = "null";

    private Retrofit conexionRetrofitSingleExecutor;

    Context contexto;
    private ImageView viewImage;

    private TextView pokemon1evolve;

    private TextView pokemon2evolve;

    private TextView pokemon3evolve;

    private ImageView pokemon1evolveImage;

    private ImageView pokemon2evolveImage;

    private ImageView pokemon3evolveImage;

    private TextView evolvesOrNot;

    private ImageView type1Image;

    private ImageView type2Image;

    private TextView viewName;


    private TextView peso;
    private TextView type1Name;

    private Switch switchShiny;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto = this;
        setContentView(R.layout.activity_detalles_pokemon);
        Intent intent = getIntent();
        pokemon = (Pokemon) intent.getSerializableExtra("pokemon");
        switchShiny = (Switch) findViewById(R.id.switchShiny);
        evolvesOrNot = (TextView) findViewById(R.id.textViewEvolvesOrNot);
        viewName = (TextView) findViewById(R.id.namePokemon);
        viewImage = (ImageView) findViewById(R.id.imagePokemon);
        type1Image = (ImageView) findViewById(R.id.type1Image);
        type2Image = (ImageView) findViewById(R.id.type2Image);
        pokemon1evolve = (TextView) findViewById(R.id.namePokemon1evolve);
        pokemon2evolve = (TextView) findViewById(R.id.namePokemon2evolve);
        pokemon3evolve = (TextView) findViewById(R.id.namePokemon3evolve);
        pokemon1evolveImage = (ImageView) findViewById(R.id.imagePokemon1evolve);
        pokemon2evolveImage = (ImageView) findViewById(R.id.imagePokemon2evolve);
        pokemon3evolveImage = (ImageView) findViewById(R.id.imagePokemon3evolve);
        peso = (TextView) findViewById(R.id.pesoPokemon);


        switchShiny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switchShiny.isChecked()){
                    setSpritePokemonShiny(pokemon.getId());
                }else{
                    setSpritePokemon(pokemon.getId());
                }
            }
        });

        setNombrePokemon(pokemon.getName().toUpperCase());
        setSpritePokemon(pokemon.getId());
        setType1Image(pokemon.getTypes().get(0).getType().getName().toUpperCase());
        peso.setText("Weight: " +String.valueOf(pokemon.getWeight()));

        if(pokemon.getTypes().size() >=2){
            setType2Image(pokemon.getTypes().get(1).getType().getName().toUpperCase());
        }else{
            type2Image.setVisibility(View.GONE);
        }

        conexionRetrofitSingleExecutor = new Retrofit.Builder()
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        String url ="";
        try{
            url = getPokeSpecies1(pokemon.getName().toLowerCase());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String [] urlSplitted = url.split("/");

        int numChain = Integer.parseInt(urlSplitted[urlSplitted.length -1]);
        ChainObject chobject = new ChainObject();
        try {
            chobject = getEvolutionChain(numChain);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if(chobject.getChain().getSpecies().getName() != null){
            pokemon1evolve.setText(chobject.getChain().getSpecies().getName().toUpperCase());
            String url1 = chobject.getChain().getSpecies().getUrl();
            String [] urlSplitted1 = url1.split("/");
            int numChain1 = Integer.parseInt(urlSplitted1[urlSplitted1.length -1]);
            Picasso.with(pokemon1evolveImage.getContext()).load(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + numChain1 + ".png"
            ).into(pokemon1evolveImage);
            if(chobject.getChain().getEvolves_to().size() > 0) {
                pokemon2evolve.setText(chobject.getChain().getEvolves_to().get(0).getSpecies().getName().toUpperCase());
                String url2 = chobject.getChain().getEvolves_to().get(0).getSpecies().getUrl();
                String [] urlSplitted2 = url2.split("/");
                int numChain2 = Integer.parseInt(urlSplitted2[urlSplitted2.length -1]);
                Picasso.with(pokemon2evolveImage.getContext()).load(
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + numChain2 + ".png"
                ).into(pokemon2evolveImage);
                if(chobject.getChain().getEvolves_to().get(0).getEvolves_to().size() > 0){
                    pokemon3evolve.setText(chobject.getChain().getEvolves_to().get(0).getEvolves_to().get(0).getSpecies().getName().toUpperCase());
                    String url3 = chobject.getChain().getEvolves_to().get(0).getEvolves_to().get(0).getSpecies().getUrl();
                    String [] urlSplitted3 = url3.split("/");
                    int numChain3 = Integer.parseInt(urlSplitted3[urlSplitted3.length -1]);
                    Picasso.with(pokemon3evolveImage.getContext()).load(
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + numChain3 + ".png"
                    ).into(pokemon3evolveImage);
                }else{
                    pokemon3evolve.setVisibility(View.GONE);
                    pokemon3evolveImage.setVisibility(View.GONE);
                }
            }else{
                pokemon1evolve.setVisibility(View.GONE);
                pokemon1evolveImage.setVisibility(View.GONE);
                pokemon2evolve.setVisibility(View.GONE);
                pokemon2evolveImage.setVisibility(View.GONE);
                pokemon3evolve.setVisibility(View.GONE);
                pokemon3evolveImage.setVisibility(View.GONE);
                evolvesOrNot.setText("No evolves");
            }
        }else{
            pokemon1evolve.setVisibility(View.GONE);
            pokemon1evolveImage.setVisibility(View.GONE);
            pokemon2evolve.setVisibility(View.GONE);
            pokemon2evolveImage.setVisibility(View.GONE);
            pokemon3evolve.setVisibility(View.GONE);
            pokemon3evolveImage.setVisibility(View.GONE);

        }
    }

    public ChainObject getEvolutionChain(int id) throws InterruptedException {
        ServicioPokeAPI servicioPokeAPI = conexionRetrofitSingleExecutor.create(ServicioPokeAPI.class);
        Call<ChainObject> chainCall = servicioPokeAPI.getEvolutionChainByChainID(id);
        final ChainObject[] chain = {new ChainObject()};
        final CountDownLatch latch = new CountDownLatch(1);
        chainCall.enqueue(new Callback<ChainObject>() {
            @Override
            public void onResponse(Call<ChainObject> call, Response<ChainObject> response) {
                chain[0] = response.body();

                latch.countDown();
            }

            @Override
            public void onFailure(Call<ChainObject> call, Throwable t) {


                latch.countDown();
            }
        });

        latch.await();
        return chain[0];
    }


    public String getPokeSpecies1(String name) throws InterruptedException {
        ServicioPokeAPI servicioPokeAPI = conexionRetrofitSingleExecutor.create(ServicioPokeAPI.class);
        Call<PokemonSpecies> pokemonSpeciesCall = servicioPokeAPI.getPokemonSpeciesByName(name.toLowerCase());
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] url = new String[1];
        pokemonSpeciesCall.enqueue(new Callback<PokemonSpecies>() {
            @Override
            public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> response) {
                if(response.isSuccessful()){
                    PokemonSpecies pspecies = response.body();
                    //System.out.println(pspecies.getEvolution_chain().getUrl());
                    url[0] = pspecies.getEvolution_chain().getUrl();
                } else {
                    Toast.makeText(contexto, "Conexion fallida get species: " + response.toString(), Toast.LENGTH_LONG).show();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                latch.countDown();
            }
        });
        latch.await();
        return url[0];
    }

    public void setType1(String type) {
        type1Name.setText(type);
    }

    public void setNombrePokemon(String nombre){
       viewName.setText(nombre.toUpperCase());
    }

    public void setSpritePokemon(int idPokemon) {
        Picasso.with(viewImage.getContext()).load(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + idPokemon + ".png"
        ).into(viewImage);

    }

    public void setSpritePokemonShiny(int idPokemon) {
        Picasso.with(viewImage.getContext()).load(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + idPokemon + ".png"
        ).into(viewImage);

    }

    public int returnIDtypes(String type){
        int id = 0;
        switch (type){
            case "NORMAL":
                id = R.drawable.normal;
                break;
            case "BUG":
                id = R.drawable.bug;
                break;
            case "DARK":
                id = R.drawable.dark;
                break;
            case "DRAGON":
                id = R.drawable.dragon;
                break;
            case "ELECTRIC":
                id = R.drawable.electric;
                break;
            case "FAIRY":
                id = R.drawable.fairy;
                break;
            case "FIRE":
                id = R.drawable.fire;
                break;
            case "FIGHTING":
                id = R.drawable.fighting;
                break;
            case "FLYING":
                id = R.drawable.flying;
                break;
            case "GHOST":
                id = R.drawable.ghost;
                break;
            case "GRASS":
                id = R.drawable.grass;
                break;
            case "GROUND":
                id = R.drawable.ground;
                break;
            case "ICE":
                id = R.drawable.ice;
                break;
            case "POISON":
                id = R.drawable.poison;
                break;
            case "PSYCHIC":
                id = R.drawable.psychic;
                break;
            case "ROCK":
                id = R.drawable.rock;
                break;
            case "STEEL":
                id = R.drawable.steel;
                break;
            case "WATER":
                id = R.drawable.water;
                break;
        }
        return id;
    }

    public void setType1Image(String type1){
        type1Image.setImageDrawable(getResources().getDrawable(returnIDtypes(type1)));
    }

    public void setType2Image(String type2){
        type2Image.setImageDrawable(getResources().getDrawable(returnIDtypes(type2)));
    }

}
