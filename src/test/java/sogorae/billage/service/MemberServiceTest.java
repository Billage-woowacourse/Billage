package sogorae.billage.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.domain.Member;
import sogorae.billage.dto.MemberSignUpRequest;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 저장 기능을 검증한다.")
    void save() {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest("sojukang@gmail.com", "sojukang",
          "12345678");

        //when
        Long savedId = memberService.save(request);

        //then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다.")
    void findByEmail() {
        // given
        String email = "boemWhale@gmail.com";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "sojukang", "12345678");
        memberService.save(request);

        // when
        Member member = memberService.findByEmail(email);

        // then
        Assertions.assertThat(member.getEmail()).isEqualTo(email);
    }
}
