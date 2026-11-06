// src/main/java/com/unicauca/edu/bancopreguntasparcial2/infrastructure/messaging/QuestionListener.java
package com.unicauca.edu.bancopreguntasparcial2.infrastructure.messaging;

import com.unicauca.edu.bancopreguntasparcial2.domain.model.Question;
import com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.repository.QuestionRepositoryJpaImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class QuestionListener {

    @Autowired
    private QuestionRepositoryJpaImpl repository;

    @RabbitListener(queues = "question.submit.review.queue")
    public void consumeSubmitQuestion(String message) {
        Long questionId = Long.parseLong(message.split(":")[1]);
        Optional<Question> questionOpt = repository.findById(questionId);
        
        if (questionOpt.isPresent()) {
            Question question = questionOpt.get();
            // Cambio de estado sin ifs condicionales (Pasa de Draft a PendingReview)
            question.getState().next(question); 
            repository.save(question);
            System.out.println("Transición ejecutada con éxito para la pregunta " + questionId);
        }
    }
}
