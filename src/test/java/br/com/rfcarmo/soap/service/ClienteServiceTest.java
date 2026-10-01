package br.com.rfcarmo.soap.service;

import br.com.rfcarmo.soap.exception.ClienteNaoEncontradoException;
import br.com.rfcarmo.soap.exception.DadosInvalidosException;
import br.com.rfcarmo.soap.model.Cliente;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClienteServiceTest {

    private final ClienteService service = new ClienteService();

    @Test
    void criaClienteComNomeEEmailNormalizados() throws Exception {
        Cliente cliente = service.criarCliente(" Ana Silva ", " ana@example.com ");

        assertEquals(1L, cliente.getId());
        assertEquals("Ana Silva", cliente.getNome());
        assertEquals("ana@example.com", cliente.getEmail());
    }

    @Test
    void rejeitaNomeVazioEEmailInvalido() {
        assertThrows(DadosInvalidosException.class,
                () -> service.criarCliente("  ", "ana@example.com"));
        assertThrows(DadosInvalidosException.class,
                () -> service.criarCliente("Ana", "email-invalido"));
    }

    @Test
    void buscaClienteEInformaQuandoNaoExiste() throws Exception {
        Cliente criado = service.criarCliente("Ana", "ana@example.com");

        assertEquals(criado.getNome(), service.buscarCliente(criado.getId()).getNome());
        assertThrows(ClienteNaoEncontradoException.class, () -> service.buscarCliente(42L));
        assertThrows(ClienteNaoEncontradoException.class, () -> service.buscarCliente(null));
    }

    @Test
    void listaEmOrdemEDevolveCopiasIndependentes() throws Exception {
        Cliente criado = service.criarCliente("Ana", "ana@example.com");
        List<Cliente> clientes = service.listarClientes();

        assertEquals(1, clientes.size());
        assertNotSame(criado, clientes.get(0));
        clientes.get(0).setNome("Nome alterado");
        clientes.clear();
        assertEquals("Ana", service.buscarCliente(criado.getId()).getNome());
        assertEquals(1, service.listarClientes().size());
    }
}
