package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PokeDetails extends AppCompatActivity {

    ImageView imageView;
    TextView nom,url;
    String mNom,mUrl;
    Button plus, like;
    String img;
    private List<Pokemon> listEquipe = new ArrayList<>();
    Pokemon current;
    Intent intent = new Intent(this,list_equipe.class);
    private SharedPreferences sharedPreferences;
    private Gson gson ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("application_esiea2", Context.MODE_PRIVATE);

        gson = new GsonBuilder()
                .setLenient()
                .create();

       listEquipe =getDataFromCache();

        setContentView(R.layout.activity_poke_details);
        imageView = findViewById(R.id.image);
        nom=findViewById(R.id.nom);
        url=findViewById(R.id.url);
        mNom=getIntent().getStringExtra("nom");
        mUrl=getIntent().getStringExtra("url");
        img =getIntent().getStringExtra("img");
        nom.setText(mNom);
        url.setText(mUrl);
        Picasso.get().load(img).resize(100, 100).into(imageView);
        current=(Pokemon) getIntent().getSerializableExtra("current");
        plus = findViewById(R.id.plus);
        like = findViewById(R.id.jaime);
        plus.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if(listEquipe.size()>=6)
                {
                    Toast.makeText(getApplicationContext(), "Vous avez deja 6 Pokemon dans votre équipe", Toast.LENGTH_SHORT).show();
                }
                else{
                    listEquipe.add(current);
                    saveList(listEquipe);
                    Toast.makeText(getApplicationContext(), "Add to Equipe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveList(List<Pokemon> pokemonList) {
        String jsonString = gson.toJson(pokemonList);
        sharedPreferences
                .edit()
                .putString("jsonListEquipe",jsonString)
                .apply();
        Toast.makeText(getApplicationContext(),"List saved",Toast.LENGTH_SHORT).show();
    }

    private List<Pokemon> getDataFromCache() {
        List<Pokemon> test = new ArrayList<>();
        String jsonPokemon = sharedPreferences.getString("jsonListEquipe",null);
        if(jsonPokemon == null)
        {
          return test;
        }
        else
        {
            Type listType = new TypeToken<List<Pokemon>>(){}.getType();
            return gson.fromJson(jsonPokemon,listType);
        }

    }

}
