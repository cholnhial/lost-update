package com.cholnhial.lostupdate.repository;

import com.cholnhial.lostupdate.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = """
        SELECT balance
        FROM account
        WHERE account_number = :an
        """,
            nativeQuery = true)
    long getBalance(@Param("an") String an);

    @Query(value = """
        UPDATE account
        SET balance = balance + :cents
        WHERE account_number = :an
        """,
            nativeQuery = true)
    @Modifying
    @Transactional
    int addBalance(@Param("an") String an, @Param("cents") long cents);
}