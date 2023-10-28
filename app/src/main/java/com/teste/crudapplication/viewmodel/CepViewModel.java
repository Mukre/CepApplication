package com.teste.crudapplication.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teste.crudapplication.models.Cep;
import com.teste.crudapplication.models.database.CepDAO;
import com.teste.crudapplication.views.CepAdapter;
import com.teste.crudapplication.views.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CepViewModel extends ViewModel {
    private MutableLiveData <String> cepDigitado = new MutableLiveData<String>();

    public void setCepDigitado(String cep){
        cepDigitado.setValue(cep);
    }
    public LiveData<String> getCepDigitado(){
        return cepDigitado;
    }

    public static boolean isValid(String cep)
    {
        Pattern p = Pattern.compile("[0-9]{5}[0-9]{3}");
        Matcher m = p.matcher(cep);
        boolean b = m.matches();
        return b;
    }

    public boolean saveCep(CepDAO dao, CepAdapter adapter, Context ctx) throws Exception {
        if (HttpRequests.searchLocation(getCepDigitado().getValue(), ctx, adapter, dao));
            return true;

    }

}
