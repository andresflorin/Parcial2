// src/main/java/com/unicauca/edu/bancopreguntasparcial2/infrastructure/jpa/repository/QuestionRepositoryJpaImpl.java
package com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.repository;

import com.unicauca.edu.bancopreguntasparcial2.domain.model.Question;
import com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.mapper.QuestionDomainMapper;
import com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.record.QuestionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class QuestionRepositoryJpaImpl {

    @Autowired
    private SpringQuestionDataRepository jpaRepository;

    public Question save(Question question) {
        QuestionRecord record = QuestionDomainMapper.toRecord(question);
        QuestionRecord savedRecord = jpaRepository.save(record);
        return QuestionDomainMapper.toDomain(savedRecord);
    }

    public Optional<Question> findById(Long id) {
        return jpaRepository.findById(id).map(QuestionDomainMapper::toDomain);
    }
}
