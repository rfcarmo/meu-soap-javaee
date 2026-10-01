package br.com.rfcarmo.soap.endpoint;

import br.com.rfcarmo.soap.exception.ClienteNaoEncontradoException;
import br.com.rfcarmo.soap.exception.DadosInvalidosException;
import br.com.rfcarmo.soap.model.Cliente;
import br.com.rfcarmo.soap.service.ClienteService;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "ClienteService",
        targetNamespace = "http://soap.rfcarmo.com.br/cliente")
public class ClienteSoapEndpoint {

    private final ClienteService clienteService;

    public ClienteSoapEndpoint() {
        this(new ClienteService());
    }

    public ClienteSoapEndpoint(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @WebMethod(operationName = "criarCliente")
    public Cliente criarCliente(
            @WebParam(name = "nome") String nome,
            @WebParam(name = "email") String email) throws DadosInvalidosException {
        return clienteService.criarCliente(nome, email);
    }

    @WebMethod(operationName = "buscarCliente")
    public Cliente buscarCliente(@WebParam(name = "id") Long id)
            throws ClienteNaoEncontradoException {
        return clienteService.buscarCliente(id);
    }

    @WebMethod(operationName = "listarClientes")
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }
}
