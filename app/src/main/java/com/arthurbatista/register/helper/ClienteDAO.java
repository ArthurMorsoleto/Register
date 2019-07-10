package com.arthurbatista.register.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.arthurbatista.register.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {

    private SQLiteDatabase dbEscreve;
    private SQLiteDatabase dbLe;

    public ClienteDAO(Context context) {

        DBHelper dbHelper = new DBHelper(context);
        dbEscreve = dbHelper.getWritableDatabase();
        dbLe = dbHelper.getReadableDatabase();
    }

    @Override
    public Boolean salvar(Cliente cliente) {

        ContentValues cv = new ContentValues();
        cv.put("nome",cliente.getNome());
        cv.put("cpf", cliente.getCpf());
        cv.put("cep", cliente.getCep());
        cv.put("logradouro", cliente.getLogradouro());
        cv.put("numero", cliente.getNumero());
        cv.put("cidade", cliente.getCidade());
        cv.put("bairro", cliente.getBairro());
        cv.put("estado", cliente.getEstado());
        cv.put("nascimento", cliente.getNascimento());

        try {
            dbEscreve.insert(DBHelper.TB_CLIENTES, null, cv);
            Log.i("INFO_DB", "salvar: Sucesso ao Salvar Cliente");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("INFO_DB", "salvar: Erro ao salvar o Cliente " + e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean atualizar(Cliente cliente) {

        ContentValues cv = new ContentValues();
        cv.put("nome",cliente.getNome());
        cv.put("cpf", cliente.getCpf());
        cv.put("cep", cliente.getCep());
        cv.put("logradouro", cliente.getLogradouro());
        cv.put("numero", cliente.getNumero());
        cv.put("cidade", cliente.getCidade());
        cv.put("bairro", cliente.getBairro());
        cv.put("estado", cliente.getEstado());
        cv.put("nascimento", cliente.getNascimento());
        try{
            String idCliente = String.valueOf(cliente.getId());
            String[] args = {idCliente};
            dbEscreve.update(DBHelper.TB_CLIENTES,cv,"id=?",args);
            Log.i("INFO", "atualizar: Sucesso ao atualizar o Cliente" );
        }catch (Exception e){
            Log.e("INFO", "atualizar: Erro ao atualizar o Cliente" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Boolean deletar(Cliente cliente) {

        try {
            String idCliente = String.valueOf(cliente.getId());
            String[] args= {idCliente};
            dbEscreve.delete(DBHelper.TB_CLIENTES, "id=?", args );
            Log.i("INFO", "deletar: Sucesso ao deletar o Cliente" );
        }catch (Exception e){
            Log.e("INFO", "deletar: Erro ao deletar o Cliente" );
            return false;
        }
        return true;
    }

    @Override
    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM " + DBHelper.TB_CLIENTES + " ;";
        Cursor c = dbLe.rawQuery(sql, null);
        while(c.moveToNext()){

            Cliente cliente = new Cliente();
            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeCliente = c.getString(c.getColumnIndex("nome"));
            String cpfCliente = c.getString(c.getColumnIndex("cpf"));
            String logradouroCliente = c.getString(c.getColumnIndex("logradouro"));
            String cepCliente = c.getString(c.getColumnIndex("cep"));
            String numeroCliente = c.getString(c.getColumnIndex("numero")) ;
            String bairroCliente = c.getString(c.getColumnIndex("bairro")) ;
            String estadoCliente = c.getString(c.getColumnIndex("estado")) ;
            String nascCliente = c.getString(c.getColumnIndex("nascimento")) ;
            String cidadeCliente = c.getString(c.getColumnIndex("cidade"));

            cliente.setId(id);
            cliente.setNome(nomeCliente);
            cliente.setCpf(cpfCliente);
            cliente.setCep(cepCliente);
            cliente.setLogradouro(logradouroCliente);
            cliente.setNumero(numeroCliente);
            cliente.setBairro(bairroCliente);
            cliente.setEstado(estadoCliente);
            cliente.setCidade(cidadeCliente);
            cliente.setNascimento(nascCliente);

            clientes.add(cliente);
        }
        return clientes;
    }
}
