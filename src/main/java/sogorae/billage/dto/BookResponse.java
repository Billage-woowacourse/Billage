package sogorae.billage.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sogorae.billage.domain.Book;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String nickname;
    private String title;
    private String imageUrl;
    private String detailMessage;
    private String location;

    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getMember().getNickname(), book.getTitle(),
            book.getImageUrl(), book.getDetailMessage(), book.getLocation());
    }
}
