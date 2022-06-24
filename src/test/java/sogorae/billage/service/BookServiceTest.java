package sogorae.billage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.controller.AllowOrDeny;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Lent;
import sogorae.billage.domain.LentStatus;
import sogorae.billage.domain.Member;
import sogorae.billage.dto.BookClientResponse;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.BookResponse;
import sogorae.billage.dto.BookUpdateRequest;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.exception.BookInvalidException;
import sogorae.billage.exception.BookNotFoundException;
import sogorae.billage.repository.LentRepository;
import sogorae.billage.service.dto.ServiceBookUpdateRequest;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LentRepository lentRepository;

    @Test
    @DisplayName("유저 이메일, 토큰 BookRegisterRequest를 입력 받아, Book을 저장한다.")
    void save() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");

        // when
        Long bookId = bookService.register(bookRegisterRequest, email);

        // then
        assertThat(bookId).isNotNull();
    }

    @Test
    @DisplayName("bookId와 사용자 email을 입력 받아, 대여 요청한다.")
    void requestLent() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        String clientEmail = "sojukang@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "sojukang",
          "Password");
        memberService.save(request);
        memberService.save(clientRequest);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, email);

        // when
        bookService.requestLent(bookId, clientEmail);
        Book book = bookService.findById(bookId);
        Lent lent = lentRepository.findByBook(book);

        // then
        assertThat(book.isLentAvailable()).isFalse();
        assertThat(lent.getStatus()).isEqualTo(LentStatus.REQUEST);
    }

    @Test
    @DisplayName("책을 등록한 멤버가 본인의 책을 대여 요청할 시 예외가 발생한다.")
    void requestLentExceptionInvalidMember() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, email);

        // when, then
        assertThatThrownBy(() -> bookService.requestLent(bookId, email))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("대여 요청을 할 수 없습니다.");
    }

    @Test
    @DisplayName("bookId와 사용자 email을 입력 받아, 대여 요청을 수락한다.")
    void allowLent() {
        // given
        String ownerEmail = "beomWhale@naver.com";
        MemberSignUpRequest ownerRequest = new MemberSignUpRequest(ownerEmail, "beom", "Password");
        memberService.save(ownerRequest);
        String clientEmail = "sojukang@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "sojukang", "Password");
        memberService.save(clientRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, ownerEmail);

        // when
        bookService.requestLent(bookId, clientEmail);
        bookService.allowOrDeny(bookId, ownerEmail, AllowOrDeny.ALLOW);

        // then
        Book book = bookService.findById(bookId);
        Lent lent = lentRepository.findByBook(book);
        assertThat(book.isLentAvailable()).isFalse();
        assertThat(lent.getStatus()).isEqualTo(LentStatus.LENT);
    }

    @Test
    @DisplayName("bookId와 사용자 email을 입력 받아, 대여 요청을 거절한다.")
    void denyLent() {
        // given
        String ownerEmail = "beomWhale@naver.com";
        MemberSignUpRequest ownerRequest = new MemberSignUpRequest(ownerEmail, "beom", "Password");
        memberService.save(ownerRequest);
        String clientEmail = "sojukang@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "sojukang", "Password");
        memberService.save(clientRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
            "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, ownerEmail);

        // when
        bookService.requestLent(bookId, clientEmail);
        bookService.allowOrDeny(bookId, ownerEmail, AllowOrDeny.DENY);

        // then
        Book book = bookService.findById(bookId);
        assertThat(book.isLentAvailable()).isTrue();
    }

    @Test
    @DisplayName("대여 요청 수락 시, owner가 아니면 예외가 발생한다.")
    void allowLentExceptionNotAuthority() {
        // given
        String ownerEmail = "beomWhale@naver.com";
        MemberSignUpRequest ownerRequest = new MemberSignUpRequest(ownerEmail, "beom", "Password");
        memberService.save(ownerRequest);
        String clientEmail = "sojukang@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "sojukang", "Password");
        memberService.save(clientRequest);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, ownerEmail);

        // when
        bookService.requestLent(bookId, clientEmail);
        assertThatThrownBy(() -> bookService.allowOrDeny(bookId, clientEmail, AllowOrDeny.ALLOW))
          .isInstanceOf(BookInvalidException.class)
          .hasMessage("책 대여 요청을 수락할 권한이 없습니다.");
    }

    @Test
    @DisplayName("대여 요청 거절 시, owner가 아니면 예외가 발생한다.")
    void denyLentExceptionNotAuthority() {
        // given
        String ownerEmail = "beomWhale@naver.com";
        MemberSignUpRequest ownerRequest = new MemberSignUpRequest(ownerEmail, "beom", "Password");
        memberService.save(ownerRequest);
        String clientEmail = "sojukang@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "sojukang", "Password");
        memberService.save(clientRequest);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
            "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, ownerEmail);

        // when
        bookService.requestLent(bookId, clientEmail);
        assertThatThrownBy(() -> bookService.allowOrDeny(bookId, clientEmail, AllowOrDeny.DENY))
            .isInstanceOf(BookInvalidException.class)
            .hasMessage("책 대여 요청을 거절할 권한이 없습니다.");
    }

    @Test
    @DisplayName("BookId를 입력 받아 조회한다.")
    void findById() {
        // given
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest("email@naver.com", "nickname", "password");
        memberService.save(signUpRequest);
        BookRegisterRequest request = new BookRegisterRequest("책 제목", "image_url", "책 상세 메세지", "책 위치");
        Long savedId = bookService.register(request, signUpRequest.getEmail());

        // when
        Book foundBook = bookService.findById(savedId);

        // then
        assertThat(foundBook.getId()).isEqualTo(savedId);
    }

    @Test
    @DisplayName("없는 BookId를 입력 받아 조회할 경우 예외가 발생한다.")
    void findByIdExceptionNotFound() {
        // when, then
        assertThatThrownBy(() -> bookService.findById(99L))
            .isInstanceOf(BookNotFoundException.class)
            .hasMessage("해당 책이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("전체 책을 조회한다.")
    void findAll() {
        // given
        Member member = new Member("email@naver.com", "nickname", "password");
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest(
            member.getEmail(), member.getNickname(), member.getPassword());
        memberService.save(signUpRequest);
        BookRegisterRequest request = new BookRegisterRequest("책 제목", "image_url", "책 상세 메세지", "책 위치");
        BookRegisterRequest request2 = new BookRegisterRequest("책 제목2", "image_url2", "책 상세 메세지2", "책 위치2");
        bookService.register(request, signUpRequest.getEmail());
        bookService.register(request2, signUpRequest.getEmail());

        // when
        List<BookResponse> books = bookService.findAll();

        // then
        Assertions.assertAll(
            () -> assertThat(request.getTitle()).isEqualTo(books.get(0).getTitle()),
            () -> assertThat(request2.getTitle()).isEqualTo(books.get(1).getTitle())
        );
    }

    @Test
    @DisplayName("사용자 email과 bookId를 입력 받아, 책을 반납한다.")
    void returning() {
        // given
        String ownerEmail = "beomWhale@naver.com";
        MemberSignUpRequest ownerRequest = new MemberSignUpRequest(ownerEmail, "beom", "Password");
        memberService.save(ownerRequest);
        String clientEmail = "sojukang@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "sojukang", "Password");
        memberService.save(clientRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long savedId = bookService.register(bookRegisterRequest, ownerEmail);

        bookService.requestLent(savedId, clientEmail);
        bookService.allowOrDeny(savedId, ownerEmail, AllowOrDeny.ALLOW);

        // when
        bookService.returning(savedId, ownerEmail);
        Book foundBook = bookService.findById(savedId);

        // then
        assertThat(foundBook.isLentAvailable()).isTrue();
    }

    @Test
    @DisplayName("사용자 email과 bookId를 입력 받아, 책을 제거한다.")
    void changeToInactive() {
        // given
        String ownerEmail = "beomWhale@naver.com";
        MemberSignUpRequest ownerRequest = new MemberSignUpRequest(ownerEmail, "beom", "Password");
        memberService.save(ownerRequest);
        String clientEmail = "sojukang@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "sojukang", "Password");
        memberService.save(clientRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long savedId = bookService.register(bookRegisterRequest, ownerEmail);


        // when, then
        bookService.changeToInactive(savedId, ownerEmail);
        assertThatThrownBy(() -> bookService.findById(savedId))
            .isInstanceOf(BookNotFoundException.class)
            .hasMessage("해당 책이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("오너 email과 BookUpdateInfoRequest를 입력 받아 책의 정보를 수정한다.")
    void updateInformation() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, email);

        // when
        String location = "위치";
        String detailMessage = "상세 메세지";
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(location, detailMessage);
        ServiceBookUpdateRequest serviceBookUpdateRequest = ServiceBookUpdateRequest.from(
          bookUpdateRequest, bookId,
          email);
        bookService.updateInformation(serviceBookUpdateRequest);

        // then
        Book foundBook = bookService.findById(bookId);
        assertAll(
          () -> assertThat(foundBook.getLocation()).isEqualTo(location),
          () -> assertThat(foundBook.getDetailMessage()).isEqualTo(detailMessage)
        );
    }

    @Test
    @DisplayName("오너 email을 입력 받아, 빌림 요청 상태의 책들을 조회한다.")
    void findAllByAvailableStatus() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        String clientEmail = "client@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "client", "Password");
        memberService.save(clientRequest);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, email);
        bookService.requestLent(bookId, clientEmail);

        // when
        List<BookResponse> books = bookService.findAllByPendingStatus(email);

        // then
        assertThat(books.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("오너 email을 입력 받아, 빌림 요청 상태의 책들을 조회한다.")
    void findAllByPendingStatus() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        String clientEmail = "client@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "client", "Password");
        memberService.save(clientRequest);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, email);
        bookService.requestLent(bookId, clientEmail);

        // when
        List<BookResponse> books = bookService.findAllByPendingStatus(email);

        // then
        assertThat(books.size()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("오너 email을 입력 받아, 빌림 상태의 책들을 조회한다.")
    void findAllByUnAvailableStatus() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        String clientEmail = "client@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "client", "Password");
        memberService.save(clientRequest);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, email);
        bookService.requestLent(bookId, clientEmail);
        bookService.allowOrDeny(bookId, email, AllowOrDeny.ALLOW);

        // when
        List<BookResponse> books = bookService.findAllByUnAvailableStatus(email);

        // then
        assertThat(books.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("email을 입력 받아, client가 빌림 요청 or 빌리고 있는 책을 조회한다.")
    void findAllByClient() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        String clientEmail = "client@naver.com";
        MemberSignUpRequest clientRequest = new MemberSignUpRequest(clientEmail, "client", "Password");
        memberService.save(clientRequest);
        String bookTitle1 = "책 제목1";
        BookRegisterRequest bookRegisterRequest1 = new BookRegisterRequest(bookTitle1, "image_url1",
          "책 상세 메세지1", "책 위치1");
        String bookTitle2 = "책 제목2";
        BookRegisterRequest bookRegisterRequest2 = new BookRegisterRequest(bookTitle2, "image_url2",
          "책 상세 메세지2", "책 위치2");
        Long bookId1 = bookService.register(bookRegisterRequest1, email);
        Long bookId2 = bookService.register(bookRegisterRequest2, email);
        bookService.requestLent(bookId1, clientEmail);
        bookService.requestLent(bookId2, clientEmail);
        bookService.allowOrDeny(bookId2, email, AllowOrDeny.ALLOW);

        // when
        List<BookClientResponse> books = bookService.findAllByClient(clientEmail);
        BookClientResponse lentBook = books.stream()
          .filter(i -> (i.getStatus().equals(LentStatus.LENT.name())))
          .findAny().get();
        BookClientResponse requestBook = books.stream()
          .filter(i -> (i.getStatus().equals(LentStatus.REQUEST.name())))
          .findAny().get();

        // then
        assertAll(
          () -> assertThat(books.size()).isEqualTo(2),
          () -> assertThat(lentBook.getTitle()).isEqualTo(bookTitle2),
          () -> assertThat(requestBook.getTitle()).isEqualTo(bookTitle1)
        );
    }
}
