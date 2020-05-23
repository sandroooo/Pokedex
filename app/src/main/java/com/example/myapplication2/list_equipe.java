package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class list_equipe extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapterEquipe mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<String> input = new ArrayList<>();
    private static final String BASE_URL = "https://pokeapi.co/";
    private SharedPreferences sharedPreferences;
    private Gson gson ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("application_esiea2", Context.MODE_PRIVATE);

        gson = new GsonBuilder()
                .setLenient()
                .create();
        List<Pokemon>pokemonList = getDataFromCache();

        if(pokemonList!= null)
        {
            ShowList(pokemonList);
        }else{
            Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
        }
    }
    private List<Pokemon> getDataFromCache() {

        String jsonPokemon = sharedPreferences.getString("jsonListEquipe",null);
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

        mAdapter = new MyAdapterEquipe(pokemonList,this);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback ItemToucherHelperCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START |ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeFlags );
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(mAdapter.values,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                saveList(mAdapter.values);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mAdapter.values.remove(position);
                mAdapter.notifyItemRemoved(position);
                saveList(mAdapter.values);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(ItemToucherHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void saveList(List<Pokemon> pokemonList) {
        String jsonString = gson.toJson(pokemonList);
        sharedPreferences
                .edit()
                .putString("jsonListEquipe",jsonString)
                .apply();
        Toast.makeText(getApplicationContext(),"List saved",Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();

    }

}
