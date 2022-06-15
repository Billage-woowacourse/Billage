package sogorae.billage.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody MemberSignUpRequest request) {
        Long createdId = memberService.save(request);
        return ResponseEntity.created(URI.create("/api/customers/" + createdId)).build();
    }
}
