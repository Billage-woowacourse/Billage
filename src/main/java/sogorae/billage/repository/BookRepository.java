package sogorae.billage.repository;

import java.util.List;

import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;
import sogorae.billage.domain.Status;

public interface BookRepository {

    Long save(Book book);

    Book findById(Long id);

    List<Book> findAll();

    List<Book> findAllByStatus(Status status, Member member);
}
