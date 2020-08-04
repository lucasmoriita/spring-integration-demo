package br.com.lucasmorita.wsintegrationdemo.soap;

import br.com.lucasmorita.wsintegrationdemo.rest.Expression;
import br.com.lucasmorita.wsintegrationdemo.soap.ws.client.generated.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.ws.DefaultSoapHeaderMapper;
import org.springframework.integration.ws.dsl.Ws;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

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
                .channel(wsRequestChannel())
                .log(LoggingHandler.Level.INFO, Message::getPayload);
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
                .<Object, Class<?>>route(Object::getClass, mapping -> mapping
                        .subFlowMapping(AddResponse.class, sf -> sf.transform(AddResponse::getAddResult))
                        .subFlowMapping(DivideResponse.class, sf -> sf.transform(DivideResponse::getDivideResult))
                        .subFlowMapping(SubtractResponse.class, sf -> sf.transform(SubtractResponse::getSubtractResult))
                        .subFlowMapping(MultiplyResponse.class, sf -> sf.transform(MultiplyResponse::getMultiplyResult)));
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
