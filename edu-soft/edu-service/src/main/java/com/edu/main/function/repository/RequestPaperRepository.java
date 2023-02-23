package com.edu.main.function.repository;

import com.edu.main.function.entity.RequestPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestPaperRepository extends JpaRepository<RequestPaper, Long> {

    List<RequestPaper> findByUsername(final String username);
}
