package sogorae.billage.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LentTest {

    @Test
    @DisplayName("빌림 요청 상태의 Lent를 빌림 완료 상태의 Lent로 변환한다.")
    void updateLent() {
        // given
        Member owner = new Member("beomWhale@naver.com", "beomWhale", "Password");
        Member client = new Member("client@naver.com", "client", "Password");
        Book book = new Book(owner, "책 제목", "image_url", "책 상세 메세지", "책 위치");
        Lent lent = new Lent(owner, client, book, LentStatus.REQUEST);

        // when
        lent.updateLent();

        // then
        assertThat(lent.getStatus()).isEqualTo(LentStatus.LENT);
    }
}
