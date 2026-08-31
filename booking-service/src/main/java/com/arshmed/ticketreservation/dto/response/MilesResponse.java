package com.arshmed.ticketreservation.dto.response;

import java.io.Serializable;
import java.util.List;

public record MilesResponse(
        String customerId,
        long totalEarned,
        long available,
        long redeemed,
        List<MilesTransactionResponse> recentTransactions
) implements Serializable {
    public record MilesTransactionResponse(
            String id,
            String bookingNumber,
            String transactionType,
            long miles,
            String description,
            String createdAt
    ) implements Serializable {}
}
