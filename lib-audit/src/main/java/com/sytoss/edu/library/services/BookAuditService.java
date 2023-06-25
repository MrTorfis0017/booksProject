package com.sytoss.edu.library.services;

import com.sytoss.edu.library.dto.BookAuditDTO;
import com.sytoss.edu.library.repositories.BookAuditRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookAuditService {

    private final BookAuditRepository bookAuditRepository;

    @Transactional
    public long saveChanges(BookAuditDTO bookAuditDTO) {
        Long lastVersion = bookAuditRepository.getLastVersion(bookAuditDTO.getBookId());
        log.debug("saveChanges: lastVersion{}", lastVersion);
        lastVersion = (lastVersion == null) ? 1 : lastVersion + 1;
        bookAuditDTO.setVersion(lastVersion);

        BookAuditDTO insertedHouseDTO = bookAuditRepository.save(bookAuditDTO);
        log.debug("saveChanges: saved BookAuditDTO with id {} bookId{} version {}"
                , insertedHouseDTO.getId(), insertedHouseDTO.getBookId(), insertedHouseDTO.getVersion());
        return insertedHouseDTO.getId();
    }

}