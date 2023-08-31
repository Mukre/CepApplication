package com.teste.crudapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CepDAO {
    private final String TABLE_CEP = "tb_cep";
    private final DbGateway gw;

    public CepDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public boolean salvar(Cep cep){
        Cursor cursor = gw.getDb().rawQuery("SELECT * FROM "+TABLE_CEP+" ORDER BY id DESC", null);
        ContentValues cv = new ContentValues();
        cv.put("cep", cep.getCep());
        cv.put("logradouro", cep.getLogradouro());
        cv.put("bairro", cep.getBairro());
        cv.put("complemento", cep.getComplemento());
        cv.put("ddd", cep.getDdd());
        cv.put("ibge", cep.getIbge());
        cv.put("gia", cep.getGia());
        cv.put("localidade", cep.getLocalidade());
        cv.put("siafi", cep.getSiafi());
        cv.put("uf", cep.getUf());

        return gw.getDb().insert(TABLE_CEP, null, cv) > 0;
    }

    public List<Cep> retornarTodos(){
        List<Cep> ceps = new ArrayList<>();
        Cursor cursor = gw.getDb().rawQuery("SELECT * FROM "+TABLE_CEP+" ORDER BY id DESC", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String cep = cursor.getString(cursor.getColumnIndexOrThrow("cep"));

            Cep cep1 = new Cep(cep);
            cep1.setBairro(cursor.getString(cursor.getColumnIndexOrThrow("bairro")));
            cep1.setDdd(cursor.getString(cursor.getColumnIndexOrThrow("ddd")));
            cep1.setGia(cursor.getString(cursor.getColumnIndexOrThrow("gia")));
            cep1.setIbge(cursor.getString(cursor.getColumnIndexOrThrow("ibge")));
            cep1.setUf(cursor.getString(cursor.getColumnIndexOrThrow("uf")));
            cep1.setComplemento(cursor.getString(cursor.getColumnIndexOrThrow("complemento")));
            cep1.setLocalidade(cursor.getString(cursor.getColumnIndexOrThrow("localidade")));
            cep1.setLogradouro(cursor.getString(cursor.getColumnIndexOrThrow("logradouro")));
            cep1.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

            ceps.add(cep1);
        }
        return ceps;
    }

    public Cep retornarUltimo(){
        Cursor cursor = gw.getDb().rawQuery("SELECT * FROM "+TABLE_CEP+" ORDER BY id DESC", null);
        if(cursor.moveToFirst()){
            String cep = cursor.getString(cursor.getColumnIndexOrThrow("cep"));
            return new Cep(cep);
        }
        return null;
    }

    public boolean verificarCep(String cep){
        Cursor cursor = gw.getDb().rawQuery("SELECT 1 FROM "+TABLE_CEP+" WHERE cep=?", new String[]{cep});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

   public boolean excluir(String cep){
        return gw.getDb().delete(TABLE_CEP, "cep=?", new String[]{cep + ""}) > 0;
   }

    public static Cep getLocation(String stringCep) throws Exception {
        Cep cep = new Cep(stringCep);
        URL url = new URL("https://viacep.com.br/ws/"+ stringCep +"/json/");
        URLConnection conexao = url.openConnection();

        InputStream is = conexao.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String jcep = "";

        StringBuilder jsoncep = new StringBuilder();

        while((jcep = br.readLine()) != null){
            jsoncep.append(jcep);
        }
        if (!jsoncep.toString().contains("erro")) {
            Cep cepAux = new Gson().fromJson(jsoncep.toString(), Cep.class);

            cep.setBairro(cepAux.getBairro());
            cep.setComplemento(cepAux.getComplemento());
            cep.setDdd(cepAux.getDdd());
            cep.setGia(cepAux.getGia());
            cep.setIbge(cepAux.getIbge());
            cep.setLocalidade(cepAux.getLocalidade());
            cep.setLogradouro(cepAux.getLogradouro());
            cep.setUf(cepAux.getUf());
            cep.setSiafi(cepAux.getSiafi());

            return cep;
        }
        else return null;
    }

    public Cep retornarCep(String cep){
        Cursor cursor = gw.getDb().rawQuery("SELECT * FROM "+TABLE_CEP+" WHERE cep = ?", new String[]{cep});
        if(cursor.moveToFirst()){
            Cep cep1 = new Cep(cursor.getString(cursor.getColumnIndexOrThrow("cep")));
            cep1.setBairro(cursor.getString(cursor.getColumnIndexOrThrow("bairro")));
            cep1.setDdd(cursor.getString(cursor.getColumnIndexOrThrow("ddd")));
            cep1.setGia(cursor.getString(cursor.getColumnIndexOrThrow("gia")));
            cep1.setIbge(cursor.getString(cursor.getColumnIndexOrThrow("ibge")));
            cep1.setUf(cursor.getString(cursor.getColumnIndexOrThrow("uf")));
            cep1.setComplemento(cursor.getString(cursor.getColumnIndexOrThrow("complemento")));
            cep1.setLocalidade(cursor.getString(cursor.getColumnIndexOrThrow("localidade")));
            cep1.setLogradouro(cursor.getString(cursor.getColumnIndexOrThrow("logradouro")));
            cep1.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

            return cep1;
        }
        return null;
    }
}

