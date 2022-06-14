package billage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import billage.domain.Member;
import billage.dto.MemberSignUpRequest;
import billage.repository.MemberRepository;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long save(MemberSignUpRequest request) {
        Member member = new Member(request.getEmail(), request.getNickname(), request.getPassword());
        return memberRepository.save(member);
    }
}
