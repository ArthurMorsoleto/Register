package com.arthurbatista.register.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.arthurbatista.register.R;
import com.arthurbatista.register.helper.ClienteDAO;
import com.arthurbatista.register.helper.MaskEditUtil;
import com.arthurbatista.register.model.CEP;
import com.arthurbatista.register.model.Cliente;
import com.arthurbatista.register.retrofit.RetrofitConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClientActivity extends AppCompatActivity {

    public EditText editNome;
    public EditText editCPF;
    public EditText editCEP;
    public EditText editLogradouro;
    public EditText editNumero;
    public EditText editBairro;
    public EditText editCidade;
    public EditText editEstado;
    public EditText editNasc;

    public SimpleDateFormat sdf;

    private ConstraintLayout activity_add_client;

    private Cliente clienteAtual;

    private boolean atualizacaoCliente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        editNome = findViewById(R.id.editNome);
        editCPF = findViewById(R.id.editCPF);
        editCEP = findViewById(R.id.editCEP);
        editLogradouro = findViewById(R.id.editLogradouro);
        editNumero = findViewById(R.id.editNumeroCasa);
        editBairro = findViewById(R.id.editBairro);
        editCidade = findViewById(R.id.editCidade);
        editEstado = findViewById(R.id.editEstado);
        editNasc = findViewById(R.id.editNasc);
        activity_add_client = findViewById(R.id.activity_add_client);

        clienteAtual = (Cliente) getIntent().getSerializableExtra("clienteSelecionado");

        if(clienteAtual != null){
            atualizacaoCliente = true;
            //Atualização de um cliente
            editNome.setText(clienteAtual.getNome());
            editCPF.setText(clienteAtual.getCpf());
            editCEP.setText(clienteAtual.getCep());
            editLogradouro.setText(clienteAtual.getLogradouro());
            editNumero.setText(clienteAtual.getNumero());
            editBairro.setText(clienteAtual.getBairro());
            editCidade.setText(clienteAtual.getCidade());
            editEstado.setText(clienteAtual.getEstado());
            editNasc.setText(clienteAtual.getNascimento());
        }

        //mascaras -- app lento
        editCPF.addTextChangedListener(MaskEditUtil.mask(editCPF, MaskEditUtil.FORMAT_CPF));
        editNasc.addTextChangedListener(MaskEditUtil.mask(editNasc, MaskEditUtil.FORMAT_DATE));
        editCEP.addTextChangedListener(MaskEditUtil.mask(editCEP, MaskEditUtil.FORMAT_CEP));

        String pattern = "dd/MM/yyyy";
        sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);

        editCEP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    Snackbar snackbar = Snackbar
                            .make(activity_add_client, "Buscando CEP", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    //buscar cep
                    RetrofitConfig retrofitConfig = new RetrofitConfig();
                    Call<CEP> call = retrofitConfig.getCEPService().findCEP(editCEP.getText().toString());
                    call.enqueue(new Callback<CEP>() {
                        @Override
                        public void onResponse(Call<CEP> call, Response<CEP> response) {
                            CEP cep = response.body();
                            if(cep!=null) {
                                editLogradouro.setText(cep.getLogradouro());
//                            editLogradouro.setEnabled(false);

                                editBairro.setText(cep.getBairro());
//                            editBairro.setEnabled(false);

                                editCidade.setText(cep.getLocalidade());
//                            editCidade.setEnabled(false);

                                editEstado.setText(cep.getUf());
//                            editEstado.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<CEP> call, Throwable t) {
                            Toast.makeText(AddClientActivity.this, "Erro ao buscar CEP: " + editCEP.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_cliente, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemSalvar:

                ClienteDAO clienteDAO = new ClienteDAO(getApplicationContext());
                Cliente cliente = new Cliente();

                //validação
                if(!validarCampos()) {
                    cliente.setNome(editNome.getText().toString());
                    cliente.setCpf(editCPF.getText().toString());
                    cliente.setCep(editCEP.getText().toString());
                    cliente.setLogradouro(editLogradouro.getText().toString());
                    cliente.setNumero(editNumero.getText().toString());
                    cliente.setBairro(editBairro.getText().toString());
                    cliente.setNascimento(editNasc.getText().toString());
                    cliente.setCidade(editCidade.getText().toString());
                    cliente.setEstado(editEstado.getText().toString());
                }
                else{
                    break;
                }

                if(atualizacaoCliente){
                    Long idClienteAtual = clienteAtual.getId();
                    cliente.setId(idClienteAtual);

                    //atualizar o bd
                    boolean result = clienteDAO.atualizar(cliente);
                    if(result){
                        Toast.makeText(this, "Sucesso ao atualizar o Cliente: \n" + cliente.getNome(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(this, "Erro ao atualizar o Cliente: \n" + cliente.getNome(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //salvar cliente novo
                    boolean result = clienteDAO.salvar(cliente);
                    if(result){
                        Toast.makeText(this, "Sucesso ao salvar o Cliente: \n" + cliente.getNome(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(this, "Erro ao salvar o Cliente: \n" + cliente.getNome(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.itemVoltar:
                finish();

            case R.id.Map:

                //TODO: verificar se os campos de endereço
//                Intent intent = new Intent(AddClientActivity.this, MapsActivity.class);
//                startActivity(intent);
                String endereco = editCEP.getText().toString();

                if(endereco.equals("")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("Digite um CEP");
                    dlg.setNeutralButton("OK",null);
                    dlg.show();
                }
                else {
                    String uri = "geo:0,0?q=" + endereco; //-23.586332, -46.658754
                    Intent intentTeste = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intentTeste);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validarCampos() {

        String nomeC        = editNome.getText().toString();
        String cpfC         = editCPF.getText().toString();
        String cepC         = editCEP.getText().toString();
        String lograC       = editLogradouro.getText().toString();
        String numeroC      = editNumero.getText().toString();
        String bairroC      = editBairro.getText().toString();
        String cidadeC      = editCidade.getText().toString();
        String dataNascC    = editNasc.getText().toString();
        String estadoC      = editEstado.getText().toString();

        boolean check = false;

        if(isEmptyValue(nomeC)){
            editNome.requestFocus();
            check = true;
        }else if(isEmptyValue(cpfC)){
            editCPF.requestFocus();
            check = true;
        }else if(isEmptyValue(cepC)){
            editCEP.requestFocus();
            check = true;
        }else if(isEmptyValue(lograC)){
            editLogradouro.requestFocus();
            check=true;
        }else if(isEmptyValue(numeroC)){
            editNumero.requestFocus();
            check = true;
        }else if(isEmptyValue(bairroC)){
            editBairro.requestFocus();
            check = true;
        }else if(isEmptyValue(cidadeC)){
            editCidade.requestFocus();
            check = true;
        }else if(isEmptyValue(estadoC)){
            editEstado.requestFocus();
            check = true;
        }else if(isEmptyValue(dataNascC) || !dateTest()){
            editNasc.requestFocus();
            check=true;
        }

        if(check){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Há campo(s) inválido(s): ");
            dlg.setNeutralButton("OK",null);
            dlg.show();
        }
        return check;
    }

    public boolean dateTest(){
        String data = editNasc.getText().toString();
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(data);
            // Data formatada corretamente
            return true;
        } catch (ParseException e) {
            // Erro de parsing!!
            return false;
        }
    }

    //verificar se os campos estão vazios
    private boolean isEmptyValue(String value){
        boolean result = false;
        if(TextUtils.isEmpty(value) || value.trim().isEmpty()){
            result = true;
        }
        return result;
    }

}
