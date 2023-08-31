package com.teste.crudapplication;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Handler;

import android.os.Bundle;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teste.crudapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.contentmain).setVisibility(View.INVISIBLE);
                findViewById(R.id.contentcep).setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            }
        });

        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.contentmain).setVisibility(View.VISIBLE);
                findViewById(R.id.contentcep).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                textViewLocation.setText("");
            }
        });

        Button btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cepValue = findViewById(R.id.editTextNumberCep);
                if (cepValue != null){
                    String cep = cepValue.getText().toString();

                    CepDAO dao = new CepDAO(getBaseContext());
                    boolean existe = dao.verificarCep(cep);
                    if (existe){
                        adapter.removerCep(dao.retornarCep(cep));
                        dao.excluir(cep);
                    }
                    boolean valida = Cep.validaCep(cep);
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

                    if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                            && valida && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
                    {

                        cepValue.setText("");

                        Snackbar sb = Snackbar.make(v, "salvando", Snackbar.LENGTH_LONG);
                        sb.setAction("Action", null)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .show();

                        Handler handler = new Handler(Looper.getMainLooper());
                        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
                        executorService1.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Cep cep1 = CepDAO.getLocation(cep);
                                    if (cep1 != null){
                                        dao.salvar(cep1);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textViewLocation.setText(cep1.toString());
                                                adapter.adicionarCep(dao.retornarCep(cep));
                                            }
                                        });
                                    }
                                    else {
                                        Snackbar.make(v, "ERRO AO SALVAR, verifique o valor inserido ou a conexão com a internet", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null)
                                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                                .show();
                                    }

                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewLocation.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                            });
                    }
                     else {
                        Snackbar.make(v, "ERRO AO SALVAR, verifique o valor inserido ou a conexão com a internet", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .show();
                    }
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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
        adapter = new CepAdapter(dao.retornarTodos());
        recyclerView.setAdapter(adapter);
        adapter.getItemCount();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
    }
}