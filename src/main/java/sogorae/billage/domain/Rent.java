package sogorae.billage.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Rent {

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
    private RentStatus status;

    public Rent(Member owner, Member client, Book book, RentStatus status) {
        this(null, null, owner, client, book, status);
    }

    public Rent(Member owner, Book book, RentStatus status) {
        this(null, null, owner, null, book, status);
    }
}
