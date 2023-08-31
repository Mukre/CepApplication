package com.teste.crudapplication;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CepAdapter extends RecyclerView.Adapter<CepHolder> {
    private final List<Cep> ceps;
    private CepDAO dao;

    public CepAdapter(List<Cep> ceps) {
        this.ceps = ceps;
    }


    /*onCreateViewHolder(ViewGroup parent, int viewType): Método que deverá retornar
    layout criado pelo ViewHolder já inflado em uma view.*/
    @NonNull
    @Override
    public CepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CepHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false));
    }

    /*onBindViewHolder(ViewHolder holder, int position): Método que recebe o
    ViewHolder e a posição da lista. Aqui é recuperado o objeto da lista de
    Objetos pela posição e associado à ViewHolder. É onde a mágica acontece!*/
    @Override
    public void onBindViewHolder(@NonNull CepHolder holder, int position) {
        holder.cep.setText(ceps.get(position).getCep());
        holder.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(v);
                Intent intent = activity.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                CepDAO dao = new CepDAO(activity.getBaseContext());

                activity.findViewById(R.id.contentmain).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.contentcep).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.fab).setVisibility(View.INVISIBLE);
                TextView textViewLocation = (TextView) activity.findViewById(R.id.textViewLocation);

                String holderCep = holder.cep.getText().toString();

                Cep cep = dao.retornarCep(holderCep);

                textViewLocation.setText(cep.toString());

                textViewLocation.setVisibility(View.VISIBLE);
            }
        });

    }

    /*getItemCount(): Método que deverá retornar quantos itens há na lista.
    Aconselha-se verificar se a lista não está nula como no exemplo, pois ao
    tentar recuperar a quantidade da lista nula pode gerar um erro em tempo
    de execução (NullPointerException).*/
    @Override
    public int getItemCount() {
        return ceps != null ? ceps.size() : 0;
    }

    public void adicionarCep(Cep cep){
        ceps.add(0, cep);
        notifyItemInserted(0);
    }

    public void removerCep(Cep cep){
        System.out.println(cep.toString());
        int position = ceps.indexOf(cep);
        ceps.remove(position);
        notifyItemRemoved(position);

    }

    public Activity getActivity(View v){
        Context ctx = v.getContext();
        while (ctx instanceof ContextWrapper){
            if(ctx instanceof Activity)
                return (Activity) ctx;
            ctx = ((ContextWrapper)ctx).getBaseContext();

        }
        return null;
    }
}

