package sogorae.billage.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;

@SpringBootTest
@Transactional
class HibernateBookRepositoryTest {

    @Autowired
    private HibernateBookRepository bookRepository;

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
}
