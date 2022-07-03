package com.cholnhial.lostupdate.service;

import com.cholnhial.lostupdate.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class TransferServiceWithTransactionAndIsolationTest {

    private final int threadCount;

    @Autowired
    private TransferService transferServiceWithTransactionAndIsolation;
    @Autowired
    private AccountRepository accountRepository;

    TransferServiceWithTransactionAndIsolationTest() {
        this.threadCount = 8;
    }

    @Test
    @Sql({"/data/clearAll.sql", "/data/data.sql"})
    public void testParallelExecution()
            throws InterruptedException {

        assertEquals(10L, accountRepository.getBalance("Alice-123"));
        assertEquals(0L, accountRepository.getBalance("Bob-456"));

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();

                    transferServiceWithTransactionAndIsolation.transfer(
                            "Alice-123", "Bob-456", 5L
                    );
                } catch (Exception e) {
                    log.error("Transfer failed", e);
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        startLatch.countDown();
        endLatch.await();

        log.info(
                "Alice's balance {}",
                accountRepository.getBalance("Alice-123")
        );
        log.info(
                "Bob's balance {}",
                accountRepository.getBalance("Bob-456")
        );
    }

}