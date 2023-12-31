package com.teste.crudapplication.views;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teste.crudapplication.R;

public class CepHolder extends RecyclerView.ViewHolder {
    public TextView cep;
    public ImageButton btnSearch;
    public CepHolder(@NonNull View itemView) {
        super(itemView);
        cep = itemView.findViewById(R.id.cepItem);
        btnSearch = itemView.findViewById(R.id.btnSearch);
    }
}
