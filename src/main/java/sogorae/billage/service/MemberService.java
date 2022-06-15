package sogorae.billage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sogorae.billage.domain.Member;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.repository.HibernateMemberRepository;

@Service
@Transactional
public class MemberService {

    private final HibernateMemberRepository hibernateMemberRepository;

    public MemberService(HibernateMemberRepository hibernateMemberRepository) {
        this.hibernateMemberRepository = hibernateMemberRepository;
    }

    public Long save(MemberSignUpRequest request) {
        Member member = new Member(request.getEmail(), request.getNickname(), request.getPassword());
        return hibernateMemberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return hibernateMemberRepository.findByEmail(email);
    }
}
