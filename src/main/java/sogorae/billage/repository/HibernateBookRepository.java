package sogorae.billage.repository;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sogorae.billage.domain.Book;
import sogorae.billage.exception.BookNotFoundException;

@Repository
@RequiredArgsConstructor
public class HibernateBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Long save(Book book) {
        em.persist(book);
        return book.getId();
    }

    @Override
    public Book findById(Long bookId) {
        Book book = em.find(Book.class, bookId);
        validateBookExists(book);
        return book;
    }

    private void validateBookExists(Book book) {
        if (Objects.isNull(book)) {
            throw new BookNotFoundException();
        }
    }
}
