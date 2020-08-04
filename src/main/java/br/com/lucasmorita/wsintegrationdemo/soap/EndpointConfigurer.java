package br.com.lucasmorita.wsintegrationdemo.soap;

import br.com.lucasmorita.wsintegrationdemo.rest.Expression;
import br.com.lucasmorita.wsintegrationdemo.soap.ws.client.generated.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.ws.DefaultSoapHeaderMapper;
import org.springframework.integration.ws.dsl.Ws;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.nio.file.Paths;

@EnableIntegration
@Configuration
public class EndpointConfigurer {


    @Bean
    public MessageChannel wsRequestChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow add() {
        return f -> f
                .<Expression, Add>transform(expression -> {
                    Add add = new Add();
                    add.setIntA(expression.getNumber1());
                    add.setIntB(expression.getNumber2());
                    return add;
                })
                .channel(wsRequestChannel());
    }

    @Bean
    public IntegrationFlow divide() {
        return f -> f
                .<Expression, Divide>transform(expression -> {
                    Divide divide = new Divide();
                    divide.setIntA(expression.getNumber1());
                    divide.setIntB(expression.getNumber2());
                    return divide;
                })
                .channel(wsRequestChannel());
    }

    @Bean
    public IntegrationFlow multiply() {
        return f -> f
                .<Expression, Multiply>transform(expression -> {
                    Multiply multiply = new Multiply();
                    multiply.setIntA(expression.getNumber1());
                    multiply.setIntB(expression.getNumber2());
                    return multiply;
                })
                .channel(wsRequestChannel());
    }

    @Bean
    public IntegrationFlow subtract() {
        return f -> f
                .<Expression, Subtract>transform(expression -> {
                    Subtract subtract = new Subtract();
                    subtract.setIntA(expression.getNumber1());
                    subtract.setIntB(expression.getNumber2());
                    return subtract;
                })
                .channel(wsRequestChannel());
    }

    @Bean
    public IntegrationFlow wsRequest() {
        return f -> f
                .channel(wsRequestChannel())
                .handle(Ws
                        .marshallingOutboundGateway(webServiceTemplate())
                        .encodingMode(DefaultUriBuilderFactory.EncodingMode.NONE)
                        .headerMapper(new DefaultSoapHeaderMapper())
                        .uri("http://www.dneonline.com/calculator.asmx"))
                .publishSubscribeChannel(c -> c
                        .subscribe(s -> s.transform(Operation::getResult))
                        .subscribe(s -> s.channel("file.input"))
                );

    }

    @Bean
    public IntegrationFlow file() {
        return f -> f
                .enrichHeaders(h -> h
                        .header(FileHeaders.FILENAME, "response.txt")
                        .header("directory", Paths.get(".").toAbsolutePath().normalize().toFile()))
                .<Operation, String>transform(o -> String.valueOf(o.getResult()))
                .handle(Files.outboundAdapter(m -> m.getHeaders().get("directory")));
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        return webServiceTemplate;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("br.com.lucasmorita.wsintegrationdemo.soap.ws.client.generated");
        return jaxb2Marshaller;
    }

}
