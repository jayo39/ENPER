package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.service.BookService;
import com.jnjnetwork.CUHelper.service.UserService;
import com.jnjnetwork.CUHelper.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @PostMapping("/toggle/{bookId}")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long bookId) {
        User user = U.getLoggedUser();
        if (user == null) return ResponseEntity.status(401).body("Unauthorized Action");

        Book book = bookService.findById(bookId);
        if (book == null) return ResponseEntity.status(404).body("Book not found");

        boolean isFavorite = userService.toggleFavorite(user.getId(), bookId);

        return ResponseEntity.ok().body(isFavorite);
    }
}
