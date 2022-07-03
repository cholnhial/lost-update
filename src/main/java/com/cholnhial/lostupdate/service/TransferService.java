package com.cholnhial.lostupdate.service;

import java.math.BigDecimal;

public interface TransferService {
    public boolean transfer(String fromAccount, String toAccount, long cents);
}
