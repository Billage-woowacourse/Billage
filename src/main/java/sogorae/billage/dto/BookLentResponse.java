package sogorae.billage.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Lent;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookLentResponse {

    private Long id;
    private String clientNickname;
    private String ownerNickname;
    private String title;
    private String imageUrl;
    private String detailMessage;
    private String requestMessage;
    private String location;
    private String status;

    public static BookLentResponse from(Lent lent) {
        Book book = lent.getBook();
        return new BookLentResponse(book.getId(), lent.getClientNickname(),
          lent.getOwnerNickname(), book.getTitle(), book.getImageUrl(), book.getDetailMessage(), lent.getRequestMessage(),book.getLocation(), lent.getStatus().name());
    }
}
