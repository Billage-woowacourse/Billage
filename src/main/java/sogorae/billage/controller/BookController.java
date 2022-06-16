package sogorae.billage.controller;

import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogorae.auth.dto.LoginMember;
import sogorae.auth.support.AuthenticationPrincipal;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.BookResponse;
import sogorae.billage.service.BookService;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody BookRegisterRequest bookRegisterRequest, @AuthenticationPrincipal LoginMember loginMember) {
        Long createdId = bookService.register(bookRegisterRequest, loginMember.getEmail());
        return ResponseEntity.created(URI.create("/api/books/" + createdId)).build();
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Void> requestRent(@PathVariable Long bookId, @AuthenticationPrincipal LoginMember loginMember) {
        bookService.requestRent(bookId, loginMember.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookId}/rents")
    public ResponseEntity<Void> allowRent(@PathVariable Long bookId, @AuthenticationPrincipal LoginMember loginMember) {
        bookService.allowRent(bookId, loginMember.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> findOne(@PathVariable Long bookId) {
        BookResponse response = BookResponse.from(bookService.findById(bookId));
        return ResponseEntity.ok(response);
    }
}
