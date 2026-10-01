package br.com.rfcarmo.soap;

import br.com.rfcarmo.soap.endpoint.ClienteSoapEndpoint;
import javax.xml.ws.Endpoint;

public final class Application {

    private static final String ENDPOINT_URL = "http://localhost:8080/servicos/clientes";

    private Application() {
    }

    public static void main(String[] args) {
        Endpoint endpoint = Endpoint.publish(ENDPOINT_URL, new ClienteSoapEndpoint());
        System.out.println("Serviço SOAP disponível em " + ENDPOINT_URL + "?wsdl");
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::stop));
    }
}
