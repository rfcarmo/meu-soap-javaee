package br.com.rfcarmo.soap.exception;

import javax.xml.ws.WebFault;

@WebFault(name = "ClienteNaoEncontradoFault",
        targetNamespace = "http://soap.rfcarmo.com.br/cliente")
public class ClienteNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    private final ClienteNaoEncontradoFault faultInfo;

    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
        this.faultInfo = new ClienteNaoEncontradoFault(mensagem);
    }

    public ClienteNaoEncontradoFault getFaultInfo() {
        return faultInfo;
    }
}
