package com.example.projekat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LokacijaAdapter extends RecyclerView.Adapter<LokacijaAdapter.LokacijaViewHolder>{
    List<Lokacija> listaLokacija;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Lokacija lokacija);
    }

    public LokacijaAdapter(List<Lokacija> listaLokacija, OnItemClickListener listener) {
        this.listaLokacija = listaLokacija;
        this.listener = listener;
    }

    public void setLista(List<Lokacija> novaLista) {
        this.listaLokacija = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LokacijaAdapter.LokacijaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new LokacijaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LokacijaAdapter.LokacijaViewHolder holder, int position) {
        Lokacija lokacija = listaLokacija.get(position);
        holder.tvNaziv.setText(lokacija.getNaziv());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(lokacija));
    }

    @Override
    public int getItemCount() {
        return listaLokacija.size();
    }

    public static class LokacijaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNaziv;

        public LokacijaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNaziv = itemView.findViewById(R.id.tvNaziv);
        }
    }
}
