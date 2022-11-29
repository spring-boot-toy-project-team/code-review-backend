package com.codereview.sms.repository;

import com.codereview.sms.entity.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SmsRepository extends JpaRepository<Sms,Long>, SmsRepositoryCustom {

}
