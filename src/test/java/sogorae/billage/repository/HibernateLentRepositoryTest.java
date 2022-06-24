package sogorae.billage.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Lent;
import sogorae.billage.domain.LentStatus;
import sogorae.billage.domain.Member;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class HibernateLentRepositoryTest {

    @Autowired
    private HibernateLentRepository lentRepository;

    @Autowired
    private HibernateMemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("owner, client, book, LentStatus를 입력 받아 생성한다.")
    void save() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        memberRepository.save(owner);
        Member client = new Member("client@naver.com", "client", "password");
        memberRepository.save(client);
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        bookRepository.save(book);

        // when
        Lent lent = new Lent(owner, client, book, LentStatus.REQUEST);
        lentRepository.save(lent);
        Lent expected = lentRepository.findByBook(book);

        // then
        assertThat(expected.getStatus()).isEqualTo(LentStatus.REQUEST);
    }

    @Test
    @DisplayName("book을 입력 받아 Lent를 삭제한다.")
    void deleteByBook() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        memberRepository.save(owner);
        Member client = new Member("client@naver.com", "client", "password");
        memberRepository.save(client);
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        bookRepository.save(book);

        // when
        Lent lent = new Lent(owner, client, book, LentStatus.REQUEST);
        lentRepository.save(lent);
        lentRepository.deleteByBook(book);

        // then
        assertThatThrownBy(() -> lentRepository.findByBook(book))
          .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
