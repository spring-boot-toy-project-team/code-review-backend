package com.codereview.security;

import com.codereview.member.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class MemberDetails implements UserDetails { //사용자의 정보를 담는 인터페이스
    private long memberId;
    private String nickName;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
    private Map<String, Object> attributes;

    public MemberDetails(long memberId, String nickName, String email, Collection<? extends GrantedAuthority> roles) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.email = email;
        this.roles = roles;
    }

    public static MemberDetails create(Member member){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRoleList().forEach(n -> {
            authorities.add(() -> n);
        });
        return new MemberDetails(member.getMemberId(), member.getEmail(), member.getNickName(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
