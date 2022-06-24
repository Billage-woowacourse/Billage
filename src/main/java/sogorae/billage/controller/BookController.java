package sogorae.billage.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import sogorae.auth.dto.LoginMember;
import sogorae.auth.support.AuthenticationPrincipal;
import sogorae.billage.dto.AllowLentRequest;
import sogorae.billage.dto.BookLentResponse;
import sogorae.billage.dto.BookLentRequest;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.BookResponse;
import sogorae.billage.dto.BookUpdateRequest;
import sogorae.billage.service.BookService;
import sogorae.billage.service.dto.ServiceBookUpdateRequest;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody BookRegisterRequest bookRegisterRequest,
        @AuthenticationPrincipal LoginMember loginMember) {
        Long createdId = bookService.register(bookRegisterRequest, loginMember.getEmail());
        return ResponseEntity.created(URI.create("/api/books/" + createdId)).build();
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Void> requestLent(@PathVariable Long bookId,
        @AuthenticationPrincipal LoginMember loginMember, @RequestBody BookLentRequest request) {
        bookService.requestLent(bookId, loginMember.getEmail(), request.getRequestMessage());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookId}/lents")
    public ResponseEntity<Void> allowLent(@PathVariable Long bookId,
        @RequestBody AllowLentRequest request,
        @AuthenticationPrincipal LoginMember loginMember) {
        bookService.allowOrDeny(bookId, loginMember.getEmail(), AllowOrDeny.from(request.getAllowOrDeny()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> findOne(@PathVariable Long bookId) {
        BookResponse response = BookResponse.from(bookService.findById(bookId));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        List<BookResponse> bookResponses = bookService.findAll();
        return ResponseEntity.ok(bookResponses);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponse> returning(@PathVariable Long bookId,
        @AuthenticationPrincipal LoginMember loginMember) {
        bookService.returning(bookId, loginMember.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/{bookId}")
    public ResponseEntity<BookResponse> update(@PathVariable Long bookId, @RequestBody BookUpdateRequest request, @AuthenticationPrincipal LoginMember loginMember) {
        bookService.updateInformation(ServiceBookUpdateRequest.from(request, bookId,
          loginMember.getEmail()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<BookResponse> delete(@PathVariable Long bookId, @AuthenticationPrincipal LoginMember loginMember) {
        bookService.changeToInactive(bookId, loginMember.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BookResponse>> findAllByPending(@AuthenticationPrincipal LoginMember loginMember) {
        List<BookResponse> bookResponses = bookService.findAllByPendingStatus(loginMember.getEmail());
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/unavailable")
    public ResponseEntity<List<BookResponse>> findAllByUnAvailable(@AuthenticationPrincipal LoginMember loginMember) {
        List<BookResponse> bookResponses = bookService.findAllByUnAvailableStatus(loginMember.getEmail());
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookLentResponse>> findAllByOwner(@AuthenticationPrincipal LoginMember loginMember) {
        List<BookLentResponse> bookResponses = bookService.findAllByOwner(loginMember.getEmail());
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/client")
    public ResponseEntity<List<BookLentResponse>> findAllByMemberAndLentStatus(@AuthenticationPrincipal LoginMember loginMember) {
        List<BookLentResponse> response = bookService.findAllByClient(loginMember.getEmail());
        return ResponseEntity.ok(response);
    }
}
