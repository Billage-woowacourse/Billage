package sogorae.billage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.repository.BookRepository;
import sogorae.billage.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public Long register(BookRegisterRequest bookRegisterRequest, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRegisterRequest.toBook(member);
        return bookRepository.save(book);
    }

    public void requestRent(Long bookId, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.requestRent(member);
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public void allowRent(Long bookId, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.allowRent(member);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void returning(Long bookId, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.returning(member);
    }
}
