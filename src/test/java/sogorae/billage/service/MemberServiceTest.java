package sogorae.billage.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.service.MemberService;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 저장 기능을 검증한다.")
    void save() {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest("sojukang@gmail.com", "sojukang", "12345678");

        //when
        Long savedId = memberService.save(request);

        //then
        assertThat(savedId).isNotNull();
    }
}
