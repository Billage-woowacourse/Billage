package sogorae.billage.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sogorae.billage.domain.Email;
import sogorae.billage.domain.Member;
import sogorae.billage.domain.Nickname;
import sogorae.billage.domain.Password;
import sogorae.billage.exception.MemberDuplicationException;
import sogorae.billage.exception.MemberNotFoundException;

@Repository
@RequiredArgsConstructor
public class HibernateMemberRepository implements MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Long save(Member member) {
        validateDuplication(member);
        em.persist(member);
        return member.getId();
    }

    private void validateDuplication(Member member) {
        String queryString = "select count(m.id) > 0 from Member m where m.email = :email or m.nickname = :nickname";
        TypedQuery<Boolean> query = em.createQuery(queryString, Boolean.class);
        query.setParameter("email", new Email(member.getEmail()));
        query.setParameter("nickname", new Nickname(member.getNickname()));
        boolean isDuplicated = query.getSingleResult();
        if (isDuplicated) {
            throw new MemberDuplicationException();
        }
    }

    @Override
    public Member findByEmail(String email) {
        String queryString = "select m from Member m where m.email = :email";
        TypedQuery<Member> query = em.createQuery(queryString, Member.class);
        query.setParameter("email", new Email(email));
        return query.getSingleResult();
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        String queryString = "select m from Member m where m.email = :email and m.password = :password";
        TypedQuery<Member> query = em.createQuery(queryString, Member.class);
        query.setParameter("email", new Email(email));
        query.setParameter("password", new Password(password));
        try {
            return query.getSingleResult();
        } catch (PersistenceException e) {
            throw new MemberNotFoundException(e.getCause());
        }
    }
}
