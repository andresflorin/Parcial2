// src/main/java/com/unicauca/edu/bancopreguntasparcial2/infrastructure/jpa/repository/SpringQuestionDataRepository.java
package com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.repository;

import com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.record.QuestionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringQuestionDataRepository extends JpaRepository<QuestionRecord, Long> {
}
