package sogorae.billage.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sogorae.billage.exception.BookInvalidException;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Member member;
    private String title;
    private String imageUrl;
    private String detailMessage;
    private String location;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean isActive;

    public Book(Member member, String title, String imageUrl, String detailMessage,
      String location) {
        this(null, member, title, imageUrl, detailMessage, location, Status.AVAILABLE, true);
    }

    public void requestRent(Member member) {
        if (status == Status.AVAILABLE && isActive && !this.member.isSameNickname(member)) {
            status = Status.PENDING;
            return;
        }
        throw new IllegalArgumentException("대여 요청을 할 수 없습니다.");
    }

    public void allowRent(Member member) {
        validAllowRent(member);
        status = Status.UNAVAILABLE;
    }

    private void validAllowRent(Member member) {
        if (noneMatchOwner(member)) {
            throw new BookInvalidException("책 대여 요청을 수락할 권한이 없습니다.");
        }
        if (!(status == Status.PENDING)) {
            throw new IllegalArgumentException("대여 수락을 할 수 없습니다.");
        }
    }

    public boolean isRentAvailable() {
        return status.isAvailable();
    }

    public boolean noneMatchOwner(Member member) {
        return !this.member.isSameNickname(member);
    }

    public void returning(Member owner) {
        validReturning(owner);
        status = Status.AVAILABLE;
    }

    private void validReturning(Member owner) {
        if (noneMatchOwner(owner)) {
            throw new BookInvalidException("반납 완료할 권한이 없습니다.");
        }
        if (!(status == Status.UNAVAILABLE)) {
            throw new BookInvalidException("반납할 수 있는 상태가 아닙니다.");
        }
    }

    public void changeToInactive(Member owner) {
        validChangeToInactive(owner);
        this.isActive = false;
    }

    private void validChangeToInactive(Member owner) {
        if (noneMatchOwner(owner)) {
            throw new BookInvalidException("책을 제거할 권한이 없습니다.");
        }
        if (status == Status.UNAVAILABLE) {
            throw new BookInvalidException("책을 제거할 수 있는 상태가 아닙니다.");
        }
    }

    public void updateInformation(Member member, String location, String detailMessage) {
        validUpdateInformation(member);
        this.location = location;
        this.detailMessage = detailMessage;
    }

    private void validUpdateInformation(Member owner) {
        if (noneMatchOwner(owner)) {
            throw new BookInvalidException("책을 정보를 수정할 권한이 없습니다.");
        }
    }
}
