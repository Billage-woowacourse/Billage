package sogorae.billage.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sogorae.billage.domain.Book;

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
        return em.find(Book.class, bookId);
    }
}
