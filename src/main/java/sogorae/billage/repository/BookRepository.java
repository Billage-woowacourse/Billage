package sogorae.billage.repository;

import sogorae.billage.domain.Book;

public interface BookRepository {

    Long save(Book book);

    Book findById(Long id);
}
