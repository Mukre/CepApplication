package com.teste.crudapplication.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.teste.crudapplication.models.Cep;
import com.teste.crudapplication.models.database.CepDAO;
import com.teste.crudapplication.views.CepAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpRequests {
    public static boolean  searchLocation(String stringCep, Context ctx, CepAdapter adapter, CepDAO dao) throws Exception {
        final boolean[] success = {false};
        RequestQueue volleyQueue = Volley.newRequestQueue(ctx);

        String url = "https://viacep.com.br/ws/"+ stringCep +"/json/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    String cepResponse;
                    try {
                        Cep responseCep = new Cep(response.getString("cep"));
                        responseCep.setComplemento(response.getString("complemento"));
                        responseCep.setBairro(response.getString("bairro"));
                        responseCep.setDdd(response.getString("ddd"));
                        responseCep.setGia(response.getString("gia"));
                        responseCep.setIbge(response.getString("ibge"));
                        responseCep.setLocalidade(response.getString("localidade"));
                        responseCep.setLogradouro(response.getString("logradouro"));
                        responseCep.setUf(response.getString("uf"));
                        responseCep.setSiafi(response.getString("siafi"));
                        if(dao.cepExists(responseCep.getCep())){
                            adapter.removerCep(dao.retornarCep(responseCep.getCep()));
                            dao.excluir(responseCep.getCep());
                        }
                        dao.save(responseCep);
                        adapter.adicionarCep(responseCep);
                        success[0] = true;
                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(ctx, "Houve um erro ao carregar o cep, verifique sua pesquisa", Toast.LENGTH_SHORT).show();
                        Log.e("HttpRequests", "loadCepError: ${error.localizedMessage}");
                    }
                },

                (Response.ErrorListener) error -> {
                    if (error.toString().contains("NoConnection")){
                        Toast.makeText(ctx, "Houve um erro ao carregar o cep, sem conexão com a internet", Toast.LENGTH_SHORT).show();
                        Log.e("HttpRequests", "loadCepError: ${error.localizedMessage}");
                    }
                    else {
                    Toast.makeText(ctx, "Houve um erro ao carregar o cep, verifique sua pesquisa", Toast.LENGTH_SHORT).show();
                    Log.e("HttpRequests", "loadCepError: ${error.localizedMessage}");
                    }
                }
        );
        volleyQueue.add(jsonObjectRequest);
        return success[0];
    }
}
