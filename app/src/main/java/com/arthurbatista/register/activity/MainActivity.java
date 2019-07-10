package com.arthurbatista.register.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arthurbatista.register.R;
import com.arthurbatista.register.adapter.ClienteAdapter;
import com.arthurbatista.register.helper.ClienteDAO;
import com.arthurbatista.register.helper.DBHelper;
import com.arthurbatista.register.helper.RecyclerItemClickListener;
import com.arthurbatista.register.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerListaClientes;

    private ClienteAdapter clienteAdapter;

    private List<Cliente> listaClientes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //configurar o recyclerView
        recyclerListaClientes = findViewById(R.id.recyclerListaClientes);

        //evento de clique para os itens do recyclerView
        recyclerListaClientes.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerListaClientes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Atualizar o cliente atual
                                //Recuperar o cliente para edição
                                Cliente clienteSelecionado = listaClientes.get(position);
                                Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
                                intent.putExtra("clienteSelecionado", clienteSelecionado);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //Remover a tarefa
                                final Cliente clienteSelecionado = listaClientes.get(position);
                                //alerta para confirmação
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir o Cliente: " + clienteSelecionado.getNome() + "?");
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //excluir
                                        ClienteDAO clienteDAO = new ClienteDAO(getApplicationContext());
                                        if(clienteDAO.deletar(clienteSelecionado)){
                                            carregarClientes();
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Cliente " + clienteSelecionado.getNome() + " Removido",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Erro ao remover o Cliente: " + clienteSelecionado.getNome(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                dialog.setNegativeButton("Não", null);
                                dialog.create();
                                dialog.show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Abrir tela de Cadastro
                Intent intent = new Intent(getApplicationContext(), AddClientActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        carregarClientes();
        super.onStart();
    }

    public void carregarClientes(){

        //Listar clientes
        ClienteDAO clienteDAO = new ClienteDAO(getApplicationContext());
        listaClientes = clienteDAO.listar();

        //configurar um adapter
        clienteAdapter =  new ClienteAdapter( listaClientes );

        //configurar o recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerListaClientes.setLayoutManager(layoutManager);
        recyclerListaClientes.setHasFixedSize(true);
        recyclerListaClientes.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerListaClientes.setAdapter(clienteAdapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
