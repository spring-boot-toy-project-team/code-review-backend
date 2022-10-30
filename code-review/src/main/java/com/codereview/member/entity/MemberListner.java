package com.codereview.member.entity;

import javax.persistence.PrePersist;

public class MemberListner {
    @PrePersist
    public void persist(Member member){
        member.setRole(Roles.ROLE_USER.toString());
    }
}
