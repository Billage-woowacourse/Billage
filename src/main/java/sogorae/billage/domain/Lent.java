package sogorae.billage.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Lent {

    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    private LocalDateTime createAt;

    @ManyToOne
    private Member owner;

    @ManyToOne
    private Member client;

    @ManyToOne
    private Book book;

    @Enumerated(EnumType.STRING)
    private LentStatus status;

    private String requestMessage;

    public Lent(Member owner, Member client, Book book, LentStatus status, String requestMessage) {
        this(null, null, owner, client, book, status, requestMessage);
    }

    public Lent(Member owner, Book book, LentStatus status, String requestMessage) {
        this(null, null, owner, null, book, status, requestMessage);
    }

    public void updateLent() {
        if (status == LentStatus.REQUEST) {
            status = LentStatus.LENT;
            return;
        }
        throw new IllegalArgumentException("빌림 요청 상태가 아닙니다.");
    }

    public String getClientNickname() {
        return client.getNickname();
    }

    public String getOwnerNickname() {
        return owner.getNickname();
    }

    public String getClientEmail() {
        return client.getEmail();
    }
}
