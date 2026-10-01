package br.com.rfcarmo.soap.exception;

public class DadosInvalidosFault {

    private String mensagem;

    public DadosInvalidosFault() {
    }

    public DadosInvalidosFault(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
