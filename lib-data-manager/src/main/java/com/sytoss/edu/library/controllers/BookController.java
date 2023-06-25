package com.sytoss.edu.library.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.params.UpdateBookParams;
import com.sytoss.edu.library.services.BookService;
import com.sytoss.edu.library.view.BookView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
@Tag(name = "BookController")
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "Book register")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/register")
    public Book registerBook(
            @RequestBody Book book) {
        String username = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("preferred_username");
        return bookService.registerBook(book, username);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(description = "Method that need to find book by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "500", description = "Book with this id is not exist!"),
    })
    @GetMapping("/{bookId}")
    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    public Book getBookById(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "Update the book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PatchMapping("/update")
    public ResponseEntity<HttpStatus> updateBook(
            @RequestBody UpdateBookParams updateBookParams) {
        String username = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("preferred_username");
        return bookService.updateBook(updateBookParams, username);
    }
}
