package sogorae.billage.repository;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import sogorae.billage.domain.Member;
import sogorae.billage.dto.MemberSignUpRequest;

@SpringBootTest
@Transactional
public class HibernateMemberRepositoryTest {

    @Autowired
    private HibernateMemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 기능을 검증한다.")
    void save() {
        // given
        Member member = new Member("sojukang@gmail.com", "sojukang", "12345678");

        // when
        Long savedId = memberRepository.save(member);

        // then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다.")
    void findByEmail() {
        // given
        String email = "boemWhale@gmail.com";
        Member member = new Member(email, "sojukang", "12345678");
        memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findByEmail(email);

        // then
        assertThat(foundMember.getEmail()).isEqualTo(email);
    }
}
