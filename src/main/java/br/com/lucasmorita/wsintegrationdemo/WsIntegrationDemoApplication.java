package br.com.lucasmorita.wsintegrationdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class WsIntegrationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsIntegrationDemoApplication.class, args);
	}

}
