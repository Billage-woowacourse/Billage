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
    private boolean isActive;

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

    public void allowRent() {
        if (status == Status.PENDING) {
            status = Status.UNAVAILABLE;
            return;
        }
        throw new IllegalArgumentException("대여 수락을 할 수 없습니다.");
    }

    public boolean isRentAvailable() {
        return status.isAvailable();
    }

    public boolean noneMatchOwner(Member member) {
        return !this.member.isSameNickname(member);
    }
}
