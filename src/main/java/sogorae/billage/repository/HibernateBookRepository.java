package sogorae.billage.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sogorae.billage.domain.Book;
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
        String queryString = "SELECT b from Book b WHERE b.id = :id AND b.isActive = true";
        TypedQuery<Book> query = em.createQuery(queryString, Book.class);
        query.setParameter("id", bookId);
        try {
            Book book = query.getSingleResult();
            return book;
        } catch (RuntimeException e) {
            throw new BookNotFoundException();
        }
    }

    public List<Book> findAll() {
        String jpql = "SELECT b from Book b WHERE b.isActive = true";
        return em.createQuery(jpql, Book.class).getResultList();
    }

    @Override
    public List<Book> findAllByStatus(Status status, Member member) {
        String jpql = "SELECT b from Book b WHERE b.status = :status AND b.member = :member AND b.isActive = true";
        TypedQuery<Book> query = em.createQuery(jpql, Book.class);
        query.setParameter("status", status);
        query.setParameter("member", member);
        return query.getResultList();
    }

    @Override
    public List<Book> findAllByMember(Member member) {
        String jpql = "SELECT b from Book b WHERE b.member = :member AND b.isActive = true";
        TypedQuery<Book> query = em.createQuery(jpql, Book.class);
        query.setParameter("member", member);
        return query.getResultList();
    }

}
