package com.cholnhial.lostupdate.service;

import com.cholnhial.lostupdate.repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransferServiceWithTransaction implements TransferService {

    private AccountRepository accountRepository;

    public TransferServiceWithTransaction(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public boolean transfer(String fromAccount, String toAccount, long cents) {
        boolean status = true;

        long fromBalance = accountRepository.getBalance(fromAccount);

        if(fromBalance >= cents) {
            status &= accountRepository.addBalance(
                    fromAccount, (-1) * cents
            ) > 0;

            status &= accountRepository.addBalance(
                    toAccount, cents
            ) > 0;
        }

        return status;
    }
}