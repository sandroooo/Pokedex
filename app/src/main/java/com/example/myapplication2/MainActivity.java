package com.example.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String BASE_URL = "https://pokeapi.co/";
    private SharedPreferences sharedPreferences;
    private Gson gson ;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Add to equipe", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, list_equipe.class);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences("application_esiea", Context.MODE_PRIVATE);

        gson = new GsonBuilder()
                .setLenient()
                .create();
        List<Pokemon>pokemonList =getDataFromCache();

        if(pokemonList!= null)
        {
            ShowList(pokemonList);
        }else{
            MakeApiCall();
        }
    }

    private List<Pokemon> getDataFromCache() {

       String jsonPokemon = sharedPreferences.getString("jsonPokemonList",null);
       if(jsonPokemon == null)
       {
           return null;
       }
       else
       {
           Type listType = new TypeToken<List<Pokemon>>(){}.getType();
           return gson.fromJson(jsonPokemon,listType);
       }
    }

    private void ShowList(final List<Pokemon> pokemonList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new MyAdapter(pokemonList,this);
        recyclerView.setAdapter(mAdapter);
    }

    private void MakeApiCall(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        PokeApi pokeApi = retrofit.create(PokeApi.class);

        Log.d("SAND","BEFORE CALLBACK");
        Call<RestPokemonResponse> call = pokeApi.getPokemonResponse();
        call.enqueue(new Callback<RestPokemonResponse>() {
           @Override
           public void onResponse(Call<RestPokemonResponse> call, Response<RestPokemonResponse> response) {

               Log.d("SAND","INSIDE CALLBACK");
               if(response.isSuccessful()&& response.body()!= null) {
                   List<Pokemon> pokemonList = response.body().getResults();
                   saveList(pokemonList);
                   ShowList(pokemonList);
               } else {
                  showError();
               }
           }

           @Override
           public void onFailure(Call<RestPokemonResponse> call, Throwable t) {
                showError();
           }
       });
        Log.d("SAND","AFTER CALLBACK");
    }

    private void saveList(List<Pokemon> pokemonList) {
        String jsonString = gson.toJson(pokemonList);
        sharedPreferences
                .edit()
                .putString("jsonPokemonList",jsonString)
                .apply();
        Toast.makeText(getApplicationContext(),"List saved",Toast.LENGTH_SHORT).show();
    }

    private void showError(){
        Toast.makeText(getApplicationContext(),"API Error",Toast.LENGTH_SHORT).show();
}
}