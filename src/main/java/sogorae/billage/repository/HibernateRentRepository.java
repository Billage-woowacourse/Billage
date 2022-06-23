package sogorae.billage.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;
import sogorae.billage.domain.Rent;

@Repository
@RequiredArgsConstructor
public class HibernateRentRepository implements RentRepository{

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Long save(Rent rent) {
        em.persist(rent);
        return rent.getId();
    }
}
