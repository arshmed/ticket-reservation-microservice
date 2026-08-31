package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final SeatRepository seatRepository;

    private static final BigDecimal ECONOMY_MULTIPLIER = BigDecimal.valueOf(1.0);
    private static final BigDecimal BUSINESS_MULTIPLIER = BigDecimal.valueOf(2.5);
    private static final BigDecimal FIRST_MULTIPLIER = BigDecimal.valueOf(4.0);

    private static final BigDecimal ECONOMY_DEMAND_FACTOR = BigDecimal.valueOf(0.4);
    private static final BigDecimal BUSINESS_DEMAND_FACTOR = BigDecimal.valueOf(0.3);
    private static final BigDecimal FIRST_DEMAND_FACTOR = BigDecimal.valueOf(0.2);

    public BigDecimal getClassMultiplier(String seatClass) {
        return switch (seatClass) {
            case "BUSINESS" -> BUSINESS_MULTIPLIER;
            case "FIRST" -> FIRST_MULTIPLIER;
            default -> ECONOMY_MULTIPLIER;
        };
    }

    public BigDecimal getDemandFactor(String seatClass) {
        return switch (seatClass) {
            case "BUSINESS" -> BUSINESS_DEMAND_FACTOR;
            case "FIRST" -> FIRST_DEMAND_FACTOR;
            default -> ECONOMY_DEMAND_FACTOR;
        };
    }

    public BigDecimal calculateDynamicPrice(BigDecimal basePrice, String seatClass, int totalSeats, int bookedSeats) {
        BigDecimal classMultiplier = getClassMultiplier(seatClass);
        BigDecimal classBasePrice = basePrice.multiply(classMultiplier);

        if (totalSeats == 0) return classBasePrice;

        BigDecimal occupancyRate = BigDecimal.valueOf(bookedSeats)
                .divide(BigDecimal.valueOf(totalSeats), 4, RoundingMode.HALF_UP);

        BigDecimal demandFactor = getDemandFactor(seatClass);
        BigDecimal priceIncrease = occupancyRate.multiply(demandFactor);
        BigDecimal dynamicPrice = classBasePrice.multiply(BigDecimal.ONE.add(priceIncrease));

        return dynamicPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTimeBasedDiscount(int daysUntilDeparture) {
        if (daysUntilDeparture > 60) return BigDecimal.valueOf(0.85);
        if (daysUntilDeparture > 30) return BigDecimal.valueOf(0.90);
        if (daysUntilDeparture > 14) return BigDecimal.valueOf(0.95);
        if (daysUntilDeparture > 7) return BigDecimal.valueOf(1.0);
        if (daysUntilDeparture > 3) return BigDecimal.valueOf(1.05);
        return BigDecimal.valueOf(1.10);
    }
}
