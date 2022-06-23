package sogorae.billage.service;

import java.util.List;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sogorae.billage.controller.AllowOrDeny;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;
import sogorae.billage.domain.Status;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.BookResponse;
import sogorae.billage.repository.BookRepository;
import sogorae.billage.repository.MemberRepository;
import sogorae.billage.service.dto.ServiceBookUpdateRequest;

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

    public void allowOrDeny(Long bookId, String email, AllowOrDeny allowOrDeny) {
        if (allowOrDeny.isAllow()) {
            allowRent(bookId, email);
            return;
        }
        denyRent(bookId, email);
    }

    private void allowRent(Long bookId, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.allowRent(member);
    }

    private void denyRent(Long bookId, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.denyRent(member);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void returning(Long bookId, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.returning(member);
    }

    public void changeToInactive(Long bookId, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.changeToInactive(member);
    }

    public void updateInformation(ServiceBookUpdateRequest serviceBookUpdateRequest) {
        Member member = memberRepository.findByEmail(serviceBookUpdateRequest.getEmail());
        Book book = bookRepository.findById(serviceBookUpdateRequest.getBookId());
        book.updateInformation(member, serviceBookUpdateRequest.getLocation(),
          serviceBookUpdateRequest.getDetailMessage());
    }

    public List<BookResponse> findAllByPendingStatus(String email) {
        return findAllByStatus(email, Status.PENDING);
    }

    public List<BookResponse> findAllByUnAvailableStatus(String email) {
        return findAllByStatus(email, Status.UNAVAILABLE);
    }

    private List<BookResponse> findAllByStatus(String email, Status status) {
        Member member = memberRepository.findByEmail(email);
        List<Book> books = bookRepository.findAllByStatus(status, member);
        return books.stream()
          .map(BookResponse::from)
          .collect(Collectors.toList());
    }

    public List<BookResponse> findAllByMember(String email) {
        Member member = memberRepository.findByEmail(email);
        List<Book> books = bookRepository.findAllByMember(member);
        return books.stream()
          .map(BookResponse::from)
          .collect(Collectors.toList());
    }
}
