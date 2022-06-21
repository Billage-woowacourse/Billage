package sogorae.billage.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.dto.SearchBookResponse;
import sogorae.billage.dto.SearchBookResponse.Items;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class NaverSearchServiceTest {

    @Autowired
    private NaverSearchService naverSearchService;

    @Test
    @DisplayName("책 제목을 입력 받아 검색한다.")
    void search() {
        // given
        String keyword = "객체지향";

        // when
        SearchBookResponse searchBookResponse = naverSearchService.search(keyword);
        List<Items> items = searchBookResponse.getItems();

        // then
        assertThat(items).isNotNull();
    }

}
