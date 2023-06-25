package com.sytoss.edu.library.repositories;

import com.sytoss.edu.library.dto.BookAuditDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BookAuditRepository extends JpaRepository<BookAuditDTO, Long> {

    @Query(value = "select max(version) from BOOK_AUDIT where BOOK_ID=:bookId", nativeQuery = true)
    Long getLastVersion(@Param("bookId") Long bookId);

    void deleteByBookId(long bookId);
}
