package sogorae.billage.repository;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Email;
import sogorae.billage.domain.Member;
import sogorae.billage.domain.Status;
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

    public List<Book> findAll() {
        String jpql = "SELECT b from Book b";
        return em.createQuery(jpql, Book.class).getResultList();
    }

    @Override
    public List<Book> findAllByStatus(Status status, Member member) {
        String jpql = "SELECT b from Book b WHERE b.status = :status AND b.member = :member";
        TypedQuery<Book> query = em.createQuery(jpql, Book.class);
        query.setParameter("status", status);
        query.setParameter("member", member);
        return query.getResultList();
    }


}
