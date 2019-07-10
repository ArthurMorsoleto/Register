package com.arthurbatista.register.helper;

import com.arthurbatista.register.model.Cliente;

import java.util.List;

public interface IClienteDAO {

    public Boolean salvar(Cliente cliente);
    public Boolean atualizar(Cliente cliente);
    public Boolean deletar(Cliente cliente);
    public List<Cliente> listar();

}
