package sogorae.billage.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Lent;
import sogorae.billage.domain.Member;

@Repository
@RequiredArgsConstructor
public class HibernateLentRepository implements LentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Long save(Lent lent) {
        em.persist(lent);
        return lent.getId();
    }

    @Override
    public void deleteByBook(Book book) {
        String jpql = "DELETE from Lent l WHERE l.book = :book";
        em.createQuery(jpql)
            .setParameter("book", book)
            .executeUpdate();
    }

    @Override
    public Lent findByBook(Book book) {
        String queryString = "select l from Lent l where l.book = :book";
        return em.createQuery(queryString, Lent.class)
            .setParameter("book", book)
            .getSingleResult();
    }

    @Override
    public List<Lent> findAllByClient(Member member) {
        String jpql = "SELECT l from Lent l WHERE l.client = :member";
        return em.createQuery(jpql, Lent.class)
            .setParameter("member", member)
            .getResultList();
    }

    @Override
    public List<Lent> findAllByOwner(Member member) {
        String jpql = "SELECT l from Lent l WHERE l.owner = :member";
        return em.createQuery(jpql, Lent.class)
            .setParameter("member", member)
            .getResultList();
    }
}
