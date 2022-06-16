package sogorae.billage.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Book {

    private final Member member;
    private final String title;
    private final String imageUrl;
    private final String detailMessage;
    private final String location;
    private Status status;
    private final boolean isActive;

    public Book(Member member, String title, String imageUrl, String detailMessage,
      String location) {
        this(member, title, imageUrl, detailMessage, location, Status.AVAILABLE, true);
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
}
