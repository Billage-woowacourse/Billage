package sogorae.billage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogorae.billage.dto.SearchBookResponse;
import sogorae.billage.service.NaverSearchService;

@RestController
@RequestMapping("/api/naver")
@AllArgsConstructor
public class NaverSerchController {

    private final NaverSearchService naverSearchService;

    @GetMapping("/{keyword}")
    public ResponseEntity<SearchBookResponse> search(@PathVariable String keyword) {
        SearchBookResponse response = naverSearchService.search(keyword);
        return ResponseEntity.ok(response);
    }
}
