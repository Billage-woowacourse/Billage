package sogorae.billage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sogorae.billage.domain.Member;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.repository.MemberRepository;

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
