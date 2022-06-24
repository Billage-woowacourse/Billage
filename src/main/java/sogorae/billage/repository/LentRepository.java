package sogorae.billage.repository;

import sogorae.billage.domain.Book;
import sogorae.billage.domain.Lent;

public interface LentRepository {
    Long save(Lent lent);

    void deleteByBook(Book book);

    Lent findByBook(Book book);
}
