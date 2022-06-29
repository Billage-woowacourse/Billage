package sogorae.billage.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchBookResponse {

    private Integer total;
    private List<Items> items = new ArrayList<>();

    public static class Items {
        public String title;
        public String image;
    }
}
