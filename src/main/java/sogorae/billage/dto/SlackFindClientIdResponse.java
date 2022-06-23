package sogorae.billage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackFindClientIdResponse {
    private boolean ok;
    private User user;

    private static class User {
        public String id;

        public String getId() {
            return id;
        }
    }

    public String getUserIdToString() {
        return user.id;
    }
}
