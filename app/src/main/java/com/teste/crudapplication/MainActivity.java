package com.teste.crudapplication.views;

import android.net.ConnectivityManager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teste.crudapplication.R;
import com.teste.crudapplication.viewmodel.HttpRequests;
import com.teste.crudapplication.models.database.CepDAO;
import com.teste.crudapplication.databinding.ActivityMainBinding;
import com.teste.crudapplication.viewmodel.CepViewModel;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CepDAO dao = new CepDAO(getBaseContext());
        CepViewModel viewModel = new ViewModelProvider(this).get(CepViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        TextView textViewError = findViewById(R.id.textViewError);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        Executor executor = Executors.newSingleThreadExecutor();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.contentmain).setVisibility(View.INVISIBLE);
                findViewById(R.id.contentcep).setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewError).setVisibility(View.VISIBLE);
            }
        });
        Button btnSave = findViewById(R.id.btnSalvar);
        btnSave.setAlpha(0.5f);
        Button btnCancel = findViewById(R.id.btnCancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.contentmain).setVisibility(View.VISIBLE);
                findViewById(R.id.contentcep).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                findViewById(R.id.textViewError).setVisibility(View.INVISIBLE);
                textViewLocation.setText("");
            }
        });
        EditText editText = findViewById(R.id.editTextNumberCep);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setCepDigitado(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        viewModel.getCepDigitado().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String texto) {
                if(dao.cepExists(texto) || viewModel.isValid(texto)){
                    textViewError.setText("");
                    btnSave.setAlpha(1);

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                HttpRequests.searchLocation(viewModel.getCepDigitado().getValue(), MainActivity.this, adapter, dao);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                else{
                    btnSave.setOnClickListener(null);
                    textViewError.setText("Deve ter 8 números");
                    btnSave.setAlpha(0.5f);
                }
            }
        });

        configurarRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    RecyclerView recyclerView;
    CepAdapter adapter;
    private void configurarRecycler(){
        //Configurando o gerenciador de leyout para ser uma lista
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        CepDAO dao = new CepDAO(this);
        adapter = new CepAdapter(dao.returnAll());
        recyclerView.setAdapter(adapter);
        adapter.getItemCount();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
    }
}