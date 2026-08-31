package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.entity.CustomerMiles;
import com.arshmed.ticketreservation.entity.MilesTransaction;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.repository.CustomerMilesRepository;
import com.arshmed.ticketreservation.repository.MilesTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.arshmed.ticketreservation.exception.ErrorType.CUSTOMER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MilesService {

    private final CustomerMilesRepository customerMilesRepository;
    private final MilesTransactionRepository milesTransactionRepository;

    private static final long MILES_PER_DOLLAR = 10;
    private static final long MILES_NEEDED_PER_DOLLAR = 100;

    public CustomerMiles getOrCreateMiles(String customerId) {
        return customerMilesRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    CustomerMiles miles = CustomerMiles.builder()
                            .customerId(customerId)
                            .totalEarned(0)
                            .available(0)
                            .redeemed(0)
                            .lastUpdated(LocalDateTime.now())
                            .build();
                    return customerMilesRepository.save(miles);
                });
    }

    public long calculateMilesToEarn(BigDecimal amount) {
        return amount.longValue() * MILES_PER_DOLLAR;
    }

    public BigDecimal calculateMilesValue(long miles) {
        return BigDecimal.valueOf(miles)
                .divide(BigDecimal.valueOf(MILES_NEEDED_PER_DOLLAR), 2, java.math.RoundingMode.HALF_UP);
    }

    public long calculateMilesNeeded(BigDecimal amount) {
        return amount.longValue() * MILES_NEEDED_PER_DOLLAR;
    }

    public void earnMiles(String customerId, String bookingNumber, BigDecimal amount) {
        CustomerMiles miles = getOrCreateMiles(customerId);
        long earned = calculateMilesToEarn(amount);

        miles.setTotalEarned(miles.getTotalEarned() + earned);
        miles.setAvailable(miles.getAvailable() + earned);
        miles.setLastUpdated(LocalDateTime.now());
        customerMilesRepository.save(miles);

        MilesTransaction transaction = MilesTransaction.builder()
                .customerId(customerId)
                .bookingNumber(bookingNumber)
                .transactionType("EARN")
                .miles(earned)
                .description(bookingNumber + " nolu rezervasyon ile " + earned + " mil kazanıldı")
                .createdAt(LocalDateTime.now())
                .build();
        milesTransactionRepository.save(transaction);
    }

    public BigDecimal redeemMiles(String customerId, String bookingNumber, long milesToRedeem) {
        CustomerMiles miles = getOrCreateMiles(customerId);

        if (miles.getAvailable() < milesToRedeem) {
            throw new BookingException(com.arshmed.ticketreservation.exception.ErrorType.BAD_REQUEST_ERROR);
        }

        BigDecimal discountAmount = calculateMilesValue(milesToRedeem);

        miles.setAvailable(miles.getAvailable() - milesToRedeem);
        miles.setRedeemed(miles.getRedeemed() + milesToRedeem);
        miles.setLastUpdated(LocalDateTime.now());
        customerMilesRepository.save(miles);

        MilesTransaction transaction = MilesTransaction.builder()
                .customerId(customerId)
                .bookingNumber(bookingNumber)
                .transactionType("REDEEM")
                .miles(milesToRedeem)
                .description(bookingNumber + " nolu rezervasyonda " + milesToRedeem + " mil kullanıldı (" + discountAmount + " TL indirim)")
                .createdAt(LocalDateTime.now())
                .build();
        milesTransactionRepository.save(transaction);

        return discountAmount;
    }

    public void refundMiles(String customerId, String bookingNumber) {
        CustomerMiles miles = getOrCreateMiles(customerId);
        List<MilesTransaction> bookingTransactions = milesTransactionRepository.findAllByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream().filter(t -> bookingNumber.equals(t.getBookingNumber())).toList();

        for (MilesTransaction t : bookingTransactions) {
            if ("EARN".equals(t.getTransactionType())) {
                miles.setTotalEarned(miles.getTotalEarned() - t.getMiles());
                miles.setAvailable(miles.getAvailable() - t.getMiles());
            } else if ("REDEEM".equals(t.getTransactionType())) {
                miles.setAvailable(miles.getAvailable() + t.getMiles());
                miles.setRedeemed(miles.getRedeemed() - t.getMiles());
            }
        }

        miles.setLastUpdated(LocalDateTime.now());
        customerMilesRepository.save(miles);

        MilesTransaction transaction = MilesTransaction.builder()
                .customerId(customerId)
                .bookingNumber(bookingNumber)
                .transactionType("CANCEL")
                .miles(0)
                .description(bookingNumber + " nolu rezervasyon iptal edildi, miller iade edildi")
                .createdAt(LocalDateTime.now())
                .build();
        milesTransactionRepository.save(transaction);
    }

    public List<MilesTransaction> getTransactions(String customerId) {
        return milesTransactionRepository.findAllByCustomerIdOrderByCreatedAtDesc(customerId);
    }
}
