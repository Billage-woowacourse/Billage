package sogorae.billage.repository;

import java.util.List;

import sogorae.billage.domain.Book;
import sogorae.billage.domain.Lent;
import sogorae.billage.domain.Member;

public interface LentRepository {
    Long save(Lent lent);

    void deleteByBook(Book book);

    Lent findByBook(Book book);

    List<Lent> findAllByClient(Member member);

    List<Lent> findAllByOwner(Member member);
}
