package sogorae.billage.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sogorae.billage.exception.BookInvalidException;

public class BookTest {

    @Test
    @DisplayName("책 대여를 요청하면, 대여 상태로 변경된다.")
    void rent() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");

        Member client = new Member("client@naver.com", "client", "password");

        // when
        book.requestRent(client);

        // then
        boolean actual = book.isRentAvailable();
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("책 빌림 대기 상태일 때, 대여 요청을 하면 예외가 발생한다.")
    void rentPendingBook() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");

        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);

        // when, then
        assertThatThrownBy(() -> book.requestRent(client))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("대여 요청을 할 수 없습니다.");
    }

    @Test
    @DisplayName("책 빌림 상태일 때, 대여 요청을 하면 예외가 발생한다.")
    void rentUnavailableBook() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);
        book.allowRent(owner);

        // when, then
        assertThatThrownBy(() -> book.requestRent(client))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("대여 요청을 할 수 없습니다.");
    }

    @Test
    @DisplayName("책 빌림 대기 상태인 책이 대여 요청 수락 시, 빌림 상태로 변환된다.")
    void allowRent() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);

        // when
        book.allowRent(owner);

        // then
        boolean actual = book.isRentAvailable();
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("빌림 완료 상태의 책을 대여 수락 시, 예외가 발생한다.")
    void allowRentUnavailable() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);
        book.allowRent(owner);

        // when, then
        assertThatThrownBy(() -> book.allowRent(owner))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("대여 수락을 할 수 없습니다.");
    }

    @Test
    @DisplayName("빌림 상태의 책을 반납 완료 상태로 변환한다.")
    void returning() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);
        book.allowRent(owner);

        // when
        book.returning(owner);

        // then
        boolean actual = book.isRentAvailable();
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("오너가 아닌 다른 사용자가 반납 완료 요청 시, 예외가 발생한다.")
    void returningExceptionNotAuthorization() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);
        book.allowRent(owner);

        // when && then
        assertThatThrownBy(() -> book.returning(client))
          .isInstanceOf(BookInvalidException.class)
          .hasMessage("반납 완료할 권한이 없습니다.");
    }

    @Test
    @DisplayName("오너가 아닌 다른 사용자가 반납 완료 요청 시, 예외가 발생한다.")
    void returningExceptionNotStatusUnavailable() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);

        // when && then
        assertThatThrownBy(() -> book.returning(owner))
          .isInstanceOf(BookInvalidException.class)
          .hasMessage("반납할 수 있는 상태가 아닙니다.");
    }

    @Test
    @DisplayName("오너가 책 active를 false로 변환한다.")
    void changeToInactive() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");

        // when
        book.changeToInactive(owner);
        Boolean result = book.getIsActive();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("오너가 아닌 사용자가 책 active를 false로 변환할 시, 예외가 발생한다..")
    void changeToInactiveExceptionNotOwner() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");

        // when, then
        assertThatThrownBy(() -> book.changeToInactive(client))
          .isInstanceOf(BookInvalidException.class)
          .hasMessage("책을 제거할 권한이 없습니다.");
    }

    @Test
    @DisplayName("빌림 상태의 책의 active를 false로 변환할 시, 예외가 발생한다..")
    void changeToInactiveExceptionStatusUnavailable() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Member client = new Member("client@naver.com", "client", "password");
        book.requestRent(client);
        book.allowRent(owner);

        // when, then
        assertThatThrownBy(() -> book.changeToInactive(owner))
          .isInstanceOf(BookInvalidException.class)
          .hasMessage("책을 제거할 수 있는 상태가 아닙니다.");
    }
    
    @Test
    @DisplayName("장소와 상세 메시지를 입력 받아 등록한 책의 정보를 수정한다.")
    void updateInformation() {
        // given
        Member owner = new Member("email@naver.com", "nickname", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        String location = "선릉";
        String detailMessage = "상세 메세지";

        // when
        book.updateInformation(owner, location, detailMessage);

        // then
        assertAll(
          () -> assertThat(book.getDetailMessage()).isEqualTo(detailMessage),
          () -> assertThat(book.getLocation()).isEqualTo(location)
        );
    }

    @Test
    @DisplayName("장소와 상세 메시지를 입력 받아 등록한 책의 정보를 수정한다.")
    void updateInformationExceptionNotOwner() {
        // given
        Member owner = new Member("owner@naver.com", "owner", "password");
        Member client = new Member("client@naver.com", "client", "password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        String location = "선릉";
        String detailMessage = "상세 메세지";

        // when, then
        assertThatThrownBy(() -> book.updateInformation(client, location, detailMessage))
          .isInstanceOf(BookInvalidException.class)
          .hasMessage("책을 정보를 수정할 권한이 없습니다.");
    }
}
