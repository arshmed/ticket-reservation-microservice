package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.exception.BookingException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import static com.arshmed.ticketreservation.exception.ErrorType.INVALID_DISCOUNT;

@Service
public class DiscountService {

    public record DiscountResult(BigDecimal discountRate, String discountLabel) {}

    public DiscountResult calculateDiscount(String passengerType, String discountType, String dateOfBirth) {
        if (discountType != null && !discountType.isEmpty() && !discountType.equals("NONE")) {
            validateDiscountForAge(discountType, dateOfBirth);
            return switch (discountType) {
                case "CHILD" -> new DiscountResult(BigDecimal.valueOf(0.75), "Çocuk İndirimi (%25)");
                case "YOUTH" -> new DiscountResult(BigDecimal.valueOf(0.10), "Genç İndirimi (%10)");
                case "SENIOR" -> new DiscountResult(BigDecimal.valueOf(0.20), "65+ İndirimi (%20)");
                case "TEACHER" -> new DiscountResult(BigDecimal.valueOf(0.15), "Öğretmen İndirimi (%15)");
                case "STUDENT" -> new DiscountResult(BigDecimal.valueOf(0.15), "Öğrenci İndirimi (%15)");
                case "DISABLED" -> new DiscountResult(BigDecimal.valueOf(0.30), "Engelli İndirimi (%30)");
                default -> new DiscountResult(BigDecimal.ZERO, "İndirim Yok");
            };
        }

        if (dateOfBirth != null && passengerType != null && passengerType.equals("ADULT")) {
            try {
                LocalDate birthDate = LocalDate.parse(dateOfBirth);
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                if (age <= 12) return new DiscountResult(BigDecimal.valueOf(0.75), "Çocuk İndirimi (%25)");
                if (age <= 25) return new DiscountResult(BigDecimal.valueOf(0.10), "Genç İndirimi (%10)");
                if (age >= 65) return new DiscountResult(BigDecimal.valueOf(0.20), "65+ İndirimi (%20)");
            } catch (Exception ignored) {}
        }

        return new DiscountResult(BigDecimal.ZERO, "İndirim Yok");
    }

    private void validateDiscountForAge(String discountType, String dateOfBirth) {
        if (dateOfBirth == null) return;
        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth);
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            switch (discountType) {
                case "CHILD" -> { if (age > 12) throw new BookingException(INVALID_DISCOUNT); }
                case "STUDENT" -> { if (age < 18 || age > 30) throw new BookingException(INVALID_DISCOUNT); }
                case "YOUTH" -> { if (age > 25) throw new BookingException(INVALID_DISCOUNT); }
                case "SENIOR" -> { if (age < 65) throw new BookingException(INVALID_DISCOUNT); }
            }
        } catch (BookingException e) {
            throw e;
        } catch (Exception ignored) {}
    }

    public BigDecimal applyDiscount(BigDecimal price, BigDecimal discountRate) {
        return price.multiply(BigDecimal.ONE.subtract(discountRate))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
