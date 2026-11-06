// src/main/java/com/unicauca/edu/bancopreguntasparcial2/infrastructure/api/QuestionController.java
package com.unicauca.edu.bancopreguntasparcial2.infrastructure.api;

import com.unicauca.edu.bancopreguntasparcial2.domain.model.Question;
import com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.repository.QuestionRepositoryJpaImpl;
import com.unicauca.edu.bancopreguntasparcial2.infrastructure.config.RabbitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepositoryJpaImpl repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody QuestionRequestDTO dto) {
        String validationUrl = "http://localhost:8082/validate";
        try {
            // Envío síncronico al microservicio de validación
            ResponseEntity<Map> response = restTemplate.postForEntity(validationUrl, dto.getJustification(), Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && Boolean.TRUE.equals(response.getBody().get("valid"))) {
                Question question = new Question.QuestionBuilder()
                        .text(dto.getText())
                        .justification(dto.getJustification())
                        .options(dto.getOptions())
                        .correctOption(dto.getCorrectOption())
                        .build();

                Question saved = repository.save(question);
                return new ResponseEntity<>(saved, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            // Manejo por fallo de comunicación o longitud < 15
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitQuestion(@PathVariable Long id) {
        String operationId = UUID.randomUUID().toString();
        
        // Comando AMQP asíncrono
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, "SubmitQuestionForReview:" + id);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("operationId", operationId));
    }
}
