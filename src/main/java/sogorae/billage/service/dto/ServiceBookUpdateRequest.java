package sogorae.billage.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import sogorae.billage.dto.BookUpdateRequest;

@Getter
@RequiredArgsConstructor
public class ServiceBookUpdateRequest {
    private final String location;
    private final String detailMessage;
    private final Long bookId;
    private final String email;

    public static ServiceBookUpdateRequest from(BookUpdateRequest bookUpdateRequest, Long bookId, String email) {
        return new ServiceBookUpdateRequest(bookUpdateRequest.getLocation(),
          bookUpdateRequest.getDetailMessage(), bookId, email);
    }
}
