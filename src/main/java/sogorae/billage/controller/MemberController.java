package sogorae.billage.controller;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody MemberSignUpRequest request) {
        log.info("---[MemberController create]--- "+"email: "+request.getEmail()+" nickname: "+request.getNickname());
        Long createdId = memberService.save(request);
        return ResponseEntity.created(URI.create("/api/customers/" + createdId)).build();
    }
}
