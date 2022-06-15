package sogorae.billage.repository;

import sogorae.billage.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    Member findByEmail(String email);
}
