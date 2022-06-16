package sogorae.billage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.domain.Book;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.exception.BookInvalidException;
import sogorae.billage.exception.BookNotFoundException;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

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
    void requestRent() {
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
        bookService.requestRent(bookId, clientEmail);
        Book book = bookService.findById(bookId);

        // then
        assertThat(book.isRentAvailable()).isFalse();
    }

    @Test
    @DisplayName("책을 등록한 멤버가 본인의 책을 대여 요청할 시 예외가 발생한다.")
    void requestRentExceptionInvalidMember() {
        // given
        String email = "beomWhale@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", "Password");
        memberService.save(request);
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        Long bookId = bookService.register(bookRegisterRequest, email);

        // when, then
        assertThatThrownBy(() -> bookService.requestRent(bookId, email))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("대여 요청을 할 수 없습니다.");
    }

    @Test
    @DisplayName("bookId와 사용자 email을 입력 받아, 대여 요청을 수락한다.")
    void allowRent() {
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
        bookService.requestRent(bookId, clientEmail);
        bookService.allowRent(bookId, ownerEmail);

        // then
        Book book = bookService.findById(bookId);
        assertThat(book.isRentAvailable()).isFalse();
    }

    @Test
    @DisplayName("대여 요청 수락 시, owner가 아니면 예외가 발생한다.")
    void allowRentExceptionNotAuthority() {
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
        bookService.requestRent(bookId, clientEmail);
        assertThatThrownBy(() -> bookService.allowRent(bookId, clientEmail))
          .isInstanceOf(BookInvalidException.class)
          .hasMessage("책 대여 요청을 수락할 권한이 없습니다.");
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

        bookService.requestRent(savedId, clientEmail);
        bookService.allowRent(savedId, ownerEmail);
        
        // when
        bookService.returning(savedId, ownerEmail);
        Book foundBook = bookService.findById(savedId);

        // then
        assertThat(foundBook.isRentAvailable()).isTrue();
    }

    @Test
    @DisplayName("사용자 email과 bookId를 입력 받아, 책을 반납한다.")
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


        // when
        bookService.changeToInactive(savedId, ownerEmail);
        Book foundBook = bookService.findById(savedId);

        // then
        assertThat(foundBook.getIsActive()).isFalse();
    }
}
