package br.com.lucasmorita.wsintegrationdemo.rest;

import br.com.lucasmorita.wsintegrationdemo.integration.CalculatorGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @Autowired
    private CalculatorGateway gateway;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Expression expression) {
        int add = gateway.add(expression);
        return new ResponseEntity<>(add,
                HttpStatus.ACCEPTED);
    }

    @PostMapping("/divide")
    public ResponseEntity<?> divide(@RequestBody Expression expression) {
        int divide = gateway.divide(expression);
        return new ResponseEntity<>(divide, HttpStatus.ACCEPTED);
    }

    @PostMapping("/subtract")
    public ResponseEntity<?> subtract(@RequestBody Expression expression) {
        int subtract = gateway.subtract(expression);
        return new ResponseEntity<>(subtract, HttpStatus.ACCEPTED);
    }

    @PostMapping("/multiply")
    public ResponseEntity<?> multiply(@RequestBody Expression expression) {
        int multiply = gateway.multiply(expression);
        return new ResponseEntity<>(multiply, HttpStatus.ACCEPTED);
    }
}
