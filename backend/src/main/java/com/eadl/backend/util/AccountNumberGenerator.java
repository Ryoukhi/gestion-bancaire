package com.eadl.backend.util;

import java.util.UUID;

public class AccountNumberGenerator {

    
    private AccountNumberGenerator() {
      // Default constructor
    }

    public static String generateUUID() {
        return "BNK-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

}
