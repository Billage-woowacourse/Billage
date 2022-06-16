package sogorae.billage.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;
import sogorae.billage.exception.BookNotFoundException;

@SpringBootTest
@Transactional
class HibernateBookRepositoryTest {

    @Autowired
    private HibernateBookRepository bookRepository;

    @Autowired
    private HibernateMemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 기능을 검증한다.")
    void save() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");

        // when
        Long savedId = bookRepository.save(book);

        // then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("BookId를 입력 받아 조회한다.")
    void findById() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Long savedId = bookRepository.save(book);

        // when
        Book foundBook = bookRepository.findById(savedId);

        // then
        assertThat(foundBook.getId()).isEqualTo(savedId);
    }

    @Test
    @DisplayName("없는 BookId를 입력 받아 조회할 경우 예외가 발생한다.")
    void findByIdExceptionNotFound() {
        // when, then
        assertThatThrownBy(() -> bookRepository.findById(99L))
            .isInstanceOf(BookNotFoundException.class)
            .hasMessage("해당 책이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("전체 책을 조회한다.")
    void findAll() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        memberRepository.save(owner);

        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Book book2 = new Book(owner, "책 제목2", "image_url2", "책 상세 메세지2", "책 위치2");
        bookRepository.save(book);
        bookRepository.save(book2);

        // when
        List<Book> books = bookRepository.findAll();

        // then
        Assertions.assertAll(
            () -> assertThat(book).isIn(books),
            () -> assertThat(book2).isIn(books)
        );
    }
}
