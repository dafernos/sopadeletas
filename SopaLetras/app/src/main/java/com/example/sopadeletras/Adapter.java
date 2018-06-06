package com.example.sopadeletras;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<ListaItem> items;
    private View.OnClickListener listener;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView palabras;
        public ViewHolder(View itemView) {
            super(itemView);
            palabras = itemView.findViewById(R.id.TextPal);
        }
        public void FormularioBind(ListaItem item) {
            palabras.setText(item.getNombre());
        }
    }

    // Contruye el objeto adaptador recibiendo la lista de datos
    public Adapter(@NonNull ArrayList < ListaItem > items) {
        this.items = items;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View palabra = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rowpalabrasopa, parent, false);
        palabra.setOnClickListener(this);
        return new ViewHolder(palabra);
    }

    // Se encarga de actualizar los datos de un ViewHolder ya existente.
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListaItem item = items.get(position);
        viewHolder.FormularioBind(item);
    }
    // Indica el número de elementos de la colección de datos.
    @Override
    public int getItemCount() {
        return  items.size();
    }
    // Asigna un listener
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }
}