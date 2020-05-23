package com.example.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterEquipe extends RecyclerView.Adapter<MyAdapterEquipe.ViewHolder> {
    List<Pokemon> pokemonList;
    Context context;
    List<Pokemon> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imgView;
        public RelativeLayout relative;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            imgView = (ImageView) v.findViewById(R.id.icon);
            relative = (RelativeLayout) v.findViewById(R.id.relative);
        }
    }

    public void add(int position, Pokemon item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterEquipe(List<Pokemon> myDataset,Context context) {
        this.context=context;
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterEquipe.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final  Pokemon currentPokemon = values.get(position);
        holder.txtHeader.setText(currentPokemon.getName());
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
        Picasso.get().load(currentPokemon.getImg()).resize(90, 90).into(holder.imgView);
        holder.txtFooter.setText((position+1) +" |  "+currentPokemon.getUrl());

        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PokeDetails.class);
                intent.putExtra("nom",currentPokemon.getName());
                intent.putExtra("url",currentPokemon.getUrl());
                intent.putExtra("img",currentPokemon.getImg());
                intent.putExtra("current",currentPokemon);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
