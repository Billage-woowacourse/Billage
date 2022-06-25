package sogorae.billage.service;

import java.util.List;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sogorae.billage.controller.AllowOrDeny;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;
import sogorae.billage.domain.Lent;
import sogorae.billage.domain.LentStatus;
import sogorae.billage.domain.Status;
import sogorae.billage.dto.BookLentResponse;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.BookResponse;
import sogorae.billage.repository.BookRepository;
import sogorae.billage.repository.MemberRepository;
import sogorae.billage.repository.LentRepository;
import sogorae.billage.service.dto.ServiceBookUpdateRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LentRepository lentRepository;
    private final MailSenderService mailSenderService;

    public Long register(BookRegisterRequest bookRegisterRequest, String email) {
        Member member = memberRepository.findByEmail(email);
        Book book = bookRegisterRequest.toBook(member);
        return bookRepository.save(book);
    }

    public void requestLent(Long bookId, String email, String requestMessage) {
        Member client = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.requestLent(client);
        Lent lent = new Lent(book.getMember(), client, book, LentStatus.REQUEST, requestMessage);
        lentRepository.save(lent);
        mailSenderService.send(book.getMember().getEmail(), client.getNickname()+"님이 "+book.getTitle()+"책을 빌림 요청했습니다.");
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public void allowOrDeny(Long bookId, String email, AllowOrDeny allowOrDeny) {
        if (allowOrDeny.isAllow()) {
            allowLent(bookId, email);
            return;
        }
        denyLent(bookId, email);
    }

    private void allowLent(Long bookId, String email) {
        Member owner = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.allowLent(owner);
        Lent lent = lentRepository.findByBook(book);
        lent.updateLent();
        mailSenderService.send(lent.getClient().getEmail(), owner.getNickname()+"님이 "+book.getTitle()+"책을 빌림 요청을 수락했습니다.");
    }

    private void denyLent(Long bookId, String email) {
        Member client = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.denyLent(client);
        lentRepository.deleteByBook(book);
        mailSenderService.send(client.getEmail(), book.getMember().getNickname()+"님이 "+book.getTitle()+"책을 빌림 요청을 거절했습니다.");
    }

    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
          .map(BookResponse::from)
          .collect(Collectors.toList());
    }

    public void returning(Long bookId, String email) {
        Member owner = memberRepository.findByEmail(email);
        Book book = bookRepository.findById(bookId);
        book.returning(owner);
        lentRepository.deleteByBook(book);
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

    public List<BookLentResponse> findAllByOwner(String email) {
        Member member = memberRepository.findByEmail(email);
        List<Lent> lents = lentRepository.findAllByOwner(member);
        return lents.stream()
          .map(BookLentResponse::from)
          .collect(Collectors.toList());
    }

    public List<BookLentResponse> findAllByClient(String email) {
        Member member = memberRepository.findByEmail(email);
        List<Lent> lents = lentRepository.findAllByClient(member);
        return lents.stream()
          .map(BookLentResponse::from)
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
