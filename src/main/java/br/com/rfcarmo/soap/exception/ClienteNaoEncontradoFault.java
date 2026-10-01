package br.com.rfcarmo.soap.exception;

public class ClienteNaoEncontradoFault {

    private String mensagem;

    public ClienteNaoEncontradoFault() {
    }

    public ClienteNaoEncontradoFault(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
