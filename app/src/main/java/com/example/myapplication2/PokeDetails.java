package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PokeDetails extends AppCompatActivity {

    ImageView imageView;
    TextView nom,url;
    String mNom,mUrl;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_details);
        imageView = findViewById(R.id.image);
        nom=findViewById(R.id.nom);
        url=findViewById(R.id.url);
        mNom=getIntent().getStringExtra("nom");
        mUrl=getIntent().getStringExtra("url");
        pos =getIntent().getIntExtra("position",0);
        nom.setText(mNom);
        url.setText(mUrl);
        Picasso.get().load("https://pokeres.bastionbot.org/images/pokemon/"+(pos+1)+".png").resize(90, 90).into(imageView);
    }
}
