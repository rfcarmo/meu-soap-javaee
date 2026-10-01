package br.com.rfcarmo.soap.service;

import br.com.rfcarmo.soap.exception.ClienteNaoEncontradoException;
import br.com.rfcarmo.soap.exception.DadosInvalidosException;
import br.com.rfcarmo.soap.model.Cliente;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteService {

    private final ConcurrentMap<Long, Cliente> clientes = new ConcurrentHashMap<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    public Cliente criarCliente(String nome, String email) throws DadosInvalidosException {
        String nomeNormalizado = nome == null ? null : nome.trim();
        String emailNormalizado = email == null ? null : email.trim();
        validar(nomeNormalizado, emailNormalizado);

        long id = proximoId.getAndIncrement();
        Cliente cliente = new Cliente(id, nomeNormalizado, emailNormalizado);
        clientes.put(id, cliente);
        return cliente.copiar();
    }

    public Cliente buscarCliente(Long id) throws ClienteNaoEncontradoException {
        if (id == null || id <= 0) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado.");
        }
        Cliente cliente = clientes.get(id);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado.");
        }
        return cliente.copiar();
    }

    public List<Cliente> listarClientes() {
        List<Cliente> resultado = new ArrayList<>();
        clientes.values().stream()
                .sorted(Comparator.comparing(Cliente::getId))
                .map(Cliente::copiar)
                .forEach(resultado::add);
        return resultado;
    }

    private void validar(String nome, String email) throws DadosInvalidosException {
        if (nome == null || nome.isEmpty()) {
            throw new DadosInvalidosException("O nome é obrigatório.");
        }
        if (!emailValido(email)) {
            throw new DadosInvalidosException("O email informado é inválido.");
        }
    }

    private boolean emailValido(String email) {
        if (email == null) {
            return false;
        }
        int arroba = email.indexOf('@');
        int ponto = email.indexOf('.', arroba + 1);
        if (arroba <= 0 || arroba != email.lastIndexOf('@')
                || ponto <= arroba + 1 || ponto == email.length() - 1) {
            return false;
        }
        for (int i = 0; i < email.length(); i++) {
            if (Character.isWhitespace(email.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
