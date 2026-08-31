package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.response.MilesResponse;
import com.arshmed.ticketreservation.entity.CustomerMiles;
import com.arshmed.ticketreservation.entity.MilesTransaction;
import com.arshmed.ticketreservation.service.MilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(MILES)
public class MilesController {

    private final MilesService milesService;

    @GetMapping("/{customer-id}")
    public ResponseEntity<MilesResponse> getMiles(
            @PathVariable(name = "customer-id") String customerId) {
        CustomerMiles miles = milesService.getOrCreateMiles(customerId);
        List<MilesTransaction> transactions = milesService.getTransactions(customerId);
        List<MilesResponse.MilesTransactionResponse> txList = transactions.stream()
                .map(t -> new MilesResponse.MilesTransactionResponse(
                        t.getId(), t.getBookingNumber(), t.getTransactionType(),
                        t.getMiles(), t.getDescription(),
                        t.getCreatedAt() != null ? t.getCreatedAt().toString() : ""))
                .toList();
        return ResponseEntity.ok(new MilesResponse(
                customerId, miles.getTotalEarned(), miles.getAvailable(),
                miles.getRedeemed(), txList));
    }
}
