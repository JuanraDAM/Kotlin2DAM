package com.example.proyectoevaluable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String user;
    private String password;

    public MyAdapter(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleTextView.setText("Información del usuario");
        holder.descriptionTextView.setText("Usuario: " + user + "\nContraseña: " + password);
        holder.weightTextView.setText("Peso: --"); // Puedes personalizar este campo según sea necesario
    }

    @Override
    public int getItemCount() {
        return 1; // Suponiendo que solo necesitas un CardView para mostrar la información
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, weightTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            weightTextView = itemView.findViewById(R.id.item_weight);
        }
    }
}
