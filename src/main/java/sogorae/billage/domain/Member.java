package sogorae.billage.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Password password;

    protected Member() {
    }

    public Member(String email, String nickname, String password) {
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password);
    }

    public Member(String email, String nickname) {
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.password = new Password("social_password");
    }

    public boolean isSameNickname(Member member) {
        return nickname.equals(member.nickname);
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getNickname() {
        return nickname.getNickname();
    }

    public String getPassword() {
        return password.getPassword();
    }
}
