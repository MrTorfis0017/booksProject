package com.sytoss.edu.library.repositories;

import com.sytoss.edu.library.dto.BooksAuditGenresDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BooksAuditGenresRepository extends JpaRepository<BooksAuditGenresDTO, Long> {

}
