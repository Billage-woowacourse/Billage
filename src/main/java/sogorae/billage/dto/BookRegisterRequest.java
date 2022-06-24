package sogorae.billage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sogorae.billage.domain.Book;
import sogorae.billage.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookRegisterRequest {

    private String title;
    private String imageUrl;
    private String detailMessage;
    private String location;


    public Book toBook(Member member) {
        return new Book(member, title, imageUrl, detailMessage, location);
    }
}
