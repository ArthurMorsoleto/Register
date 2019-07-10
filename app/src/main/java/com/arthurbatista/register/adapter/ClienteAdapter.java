package com.arthurbatista.register.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arthurbatista.register.R;
import com.arthurbatista.register.model.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder> {

    private List<Cliente> listaClientes;

    public ClienteAdapter(List<Cliente> lista) {
        this.listaClientes = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemLista = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_clientes_adapter, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Cliente cliente = listaClientes.get(i);
        myViewHolder.txtNomeCliente.setText(cliente.getNome());
        myViewHolder.textCPFCliente.setText(cliente.getCpf());
    }

    @Override
    public int getItemCount() {
        return this.listaClientes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textCPFCliente;
        TextView txtNomeCliente;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCPFCliente = itemView.findViewById(R.id.textCPFCliente);
            txtNomeCliente = itemView.findViewById(R.id.textNomeCliente);
        }
    }


}
