package sogorae.billage.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import sogorae.billage.domain.Email;
import sogorae.billage.domain.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HibernateMemberRepository implements MemberRepository{

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    public Member findByEmail(String email) {
        String queryString = "select m from Member m where m.email = :email";
        TypedQuery<Member> query = em.createQuery(queryString, Member.class);
        query.setParameter("email", new Email(email));
        return query.getSingleResult();
    }
}
