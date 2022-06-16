package sogorae.billage.repository;

import java.util.List;

import sogorae.billage.domain.Book;

public interface BookRepository {

    Long save(Book book);

    Book findById(Long id);

    List<Book> findAll();
}
