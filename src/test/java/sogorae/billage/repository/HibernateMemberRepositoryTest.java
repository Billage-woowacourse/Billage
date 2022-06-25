package sogorae.billage.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sogorae.billage.domain.Member;
import sogorae.billage.exception.MemberDuplicationException;

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
    @DisplayName("이메일이 이미 존재하는 회원을 저장할 시, 예외가 발생한다.")
    void saveExceptionDuplicationEmail() {
        // given
        Member member1 = new Member("sojukang@gmail.com", "sojukang", "12345678");
        Member member2 = new Member("sojukang@gmail.com", "beomWhale", "12345678");
        Long savedId = memberRepository.save(member1);

        // when
        assertThatThrownBy(() -> memberRepository.save(member2)).isInstanceOf(
            MemberDuplicationException.class)
          .hasMessage("이미 존재하는 회원입니다.");

        // then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("닉네임이 이미 존재하는 회원을 저장할 시, 예외가 발생한다.")
    void saveExceptionDuplicationNickname() {
        // given
        Member member1 = new Member("sojukang@gmail.com", "sojukang", "12345678");
        Member member2 = new Member("beom@gmail.com", "sojukang", "12345678");
        Long savedId = memberRepository.save(member1);

        // when
        assertThatThrownBy(() -> memberRepository.save(member2)).isInstanceOf(
                MemberDuplicationException.class)
            .hasMessage("이미 존재하는 회원입니다.");

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
