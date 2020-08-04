package br.com.lucasmorita.wsintegrationdemo.integration;

import br.com.lucasmorita.wsintegrationdemo.rest.Expression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.ws.WebServiceHeaders;

@MessagingGateway
public interface CalculatorGateway {

    @Gateway(requestChannel = "add.input",
            headers = @GatewayHeader(name = WebServiceHeaders.SOAP_ACTION, value = "http://tempuri.org/Add"))
    int add(Expression expression);

    @Gateway(requestChannel = "divide.input",
            headers = @GatewayHeader(name = WebServiceHeaders.SOAP_ACTION, value = "http://tempuri.org/Divide"))
    int divide(Expression expression);

    @Gateway(requestChannel = "multiply.input",
            headers = @GatewayHeader(name = WebServiceHeaders.SOAP_ACTION, value = "http://tempuri.org/Multiply"))
    int multiply(Expression expression);

    @Gateway(requestChannel = "subtract.input",
            headers = @GatewayHeader(name = WebServiceHeaders.SOAP_ACTION, value = "http://tempuri.org/Subtract"))
    int subtract(Expression expression);
}
