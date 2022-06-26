package sogorae.billage.controller;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogorae.auth.dto.LoginMember;
import sogorae.auth.support.AuthenticationPrincipal;
import sogorae.billage.dto.AllowLentRequest;
import sogorae.billage.dto.BookLentRequest;
import sogorae.billage.dto.BookLentResponse;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.BookResponse;
import sogorae.billage.dto.BookUpdateRequest;
import sogorae.billage.service.BookService;
import sogorae.billage.service.dto.ServiceBookUpdateRequest;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody BookRegisterRequest bookRegisterRequest,
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info("---[BookController register]--- " + "nickname: " + loginMember.getNickname()
          + " bookTitle: " + bookRegisterRequest.getTitle());
        Long createdId = bookService.register(bookRegisterRequest, loginMember.getEmail());
        return ResponseEntity.created(URI.create("/api/books/" + createdId)).build();
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Void> requestLent(@PathVariable Long bookId,
      @AuthenticationPrincipal LoginMember loginMember, @RequestBody BookLentRequest request) {
        log.info("---[BookController requestLent]--- " + "nickname: " + loginMember.getNickname()
          + " requestMessage: " + request.getRequestMessage());
        bookService.requestLent(bookId, loginMember.getEmail(), request.getRequestMessage());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookId}/lents")
    public ResponseEntity<Void> allowLent(@PathVariable Long bookId,
      @RequestBody AllowLentRequest request,
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info("---[BookController allowLent]--- " + "nickname: " + loginMember.getNickname());
        bookService.allowOrDeny(bookId, loginMember.getEmail(),
          AllowOrDeny.from(request.getAllowOrDeny()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> findOne(@PathVariable Long bookId) {
        log.info("---[BookController findOne]--- " + "bookId: " + bookId);
        BookResponse response = BookResponse.from(bookService.findById(bookId));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        log.info("---[BookController findAll]--- ");
        List<BookResponse> bookResponses = bookService.findAll();
        return ResponseEntity.ok(bookResponses);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponse> returning(@PathVariable Long bookId,
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info("---[BookController returning]--- " + "nickname: " + loginMember.getNickname());
        bookService.returning(bookId, loginMember.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/{bookId}")
    public ResponseEntity<BookResponse> update(@PathVariable Long bookId,
      @RequestBody BookUpdateRequest request, @AuthenticationPrincipal LoginMember loginMember) {
        bookService.updateInformation(ServiceBookUpdateRequest.from(request, bookId,
          loginMember.getEmail()));
        log.info("---[BookController update]--- " + "nickname: " + loginMember.getNickname()
          + "bookUpdateLocation: " + request.getLocation() + "bookUpdatedetailMessage: "
          + request.getDetailMessage());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<BookResponse> delete(@PathVariable Long bookId,
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info(
          "---[BookController delete]--- " + "nickname: " + loginMember.getNickname() + " bookId : "
            + bookId);
        bookService.changeToInactive(bookId, loginMember.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BookResponse>> findAllByPending(
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info(
          "---[BookController findAllByPending]--- " + "nickname: " + loginMember.getNickname());
        List<BookResponse> bookResponses = bookService.findAllByPendingStatus(
          loginMember.getEmail());
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/unavailable")
    public ResponseEntity<List<BookResponse>> findAllByUnAvailable(
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info("---[BookController findAllByUnAvailable]--- " + "nickname: "
          + loginMember.getNickname());
        List<BookResponse> bookResponses = bookService.findAllByUnAvailableStatus(
          loginMember.getEmail());
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/me/lent")
    public ResponseEntity<List<BookLentResponse>> findAllByOwner(
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info(
          "---[BookController findAllByOwner]--- " + "nickname: " + loginMember.getNickname());
        List<BookLentResponse> bookResponses = bookService.findAllByOwner(loginMember.getEmail());
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookResponse>> findAllByMember(
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info(
          "---[BookController findAllByMember]--- " + "nickname: " + loginMember.getNickname());
        List<BookResponse> bookResponses = bookService.findAllByMember(loginMember.getEmail());
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/client")
    public ResponseEntity<List<BookLentResponse>> findAllByMemberAndLentStatus(
      @AuthenticationPrincipal LoginMember loginMember) {
        log.info("---[BookController findAllByMemberAndLentStatus]--- " + "nickname: "
          + loginMember.getNickname());
        List<BookLentResponse> response = bookService.findAllByClient(loginMember.getEmail());
        return ResponseEntity.ok(response);
    }
}
