package br.com.rfcarmo.soap.exception;

import javax.xml.ws.WebFault;

@WebFault(name = "DadosInvalidosFault",
        targetNamespace = "http://soap.rfcarmo.com.br/cliente")
public class DadosInvalidosException extends Exception {

    private static final long serialVersionUID = 1L;

    private final DadosInvalidosFault faultInfo;

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
        this.faultInfo = new DadosInvalidosFault(mensagem);
    }

    public DadosInvalidosFault getFaultInfo() {
        return faultInfo;
    }
}
