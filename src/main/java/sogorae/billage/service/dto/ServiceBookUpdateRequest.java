package sogorae.billage.service.dto;

import lombok.Getter;
import sogorae.billage.dto.BookUpdateRequest;

@Getter
public class ServiceBookUpdateRequest {
    private final String location;
    private final String detailMessage;
    private final Long bookId;
    private final String email;

    private ServiceBookUpdateRequest(String location, String detailMessage, Long bookId,
      String email) {
        this.location = location;
        this.detailMessage = detailMessage;
        this.bookId = bookId;
        this.email = email;
    }

    public static ServiceBookUpdateRequest from(BookUpdateRequest bookUpdateRequest, Long bookId, String email) {
        return new ServiceBookUpdateRequest(bookUpdateRequest.getLocation(),
          bookUpdateRequest.getDetailMessage(), bookId, email);
    }
}
