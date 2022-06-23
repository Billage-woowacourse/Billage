package sogorae.billage.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;
import sogorae.billage.domain.Rent;
import sogorae.billage.domain.RentStatus;
import sogorae.billage.domain.Status;
import sogorae.billage.dto.BookResponse;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class HibernateRentRepositoryTest {

    @Autowired
    private HibernateRentRepository rentRepository;

    @Autowired
    private HibernateMemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void save() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        memberRepository.save(owner);
        Member client = new Member("client@naver.com", "client", "password");
        memberRepository.save(client);

        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Book book2 = new Book(owner, "책 제목2", "image_url2", "책 상세 메세지2", "책 위치2");
        bookRepository.save(book);
        bookRepository.save(book2);


        Rent rent = new Rent(owner, client, book, RentStatus.ALLOW);
        rentRepository.save(rent);
    }
}
