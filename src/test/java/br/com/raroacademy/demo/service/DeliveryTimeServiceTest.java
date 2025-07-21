package br.com.raroacademy.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeliveryTimeServiceTest {

    @InjectMocks
    private DeliveryTimeService deliveryTimeService;

    private LocalDate shippingDate;

    @BeforeEach
    void setUp() {
        shippingDate = LocalDate.of(2025, 7, 20);
    }

    @Test
    void calculate_NullShippingDate_ReturnsNull() {
        // Act
        LocalDate result = deliveryTimeService.calculate("SP", null);

        // Assert
        assertNull(result);
    }

    @Test
    void calculate_NullState_DefaultsToSudeste() {
        // Act
        LocalDate result = deliveryTimeService.calculate(null, shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(5), result);
    }

    @Test
    void calculate_EmptyState_DefaultsToSudeste() {
        // Act
        LocalDate result = deliveryTimeService.calculate("", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(5), result);
    }

    @Test
    void calculate_InvalidState_DefaultsToSudeste() {
        // Act
        LocalDate result = deliveryTimeService.calculate("XX", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(5), result);
    }

    @Test
    void calculate_SudesteState_Returns5Days() {
        // Act
        LocalDate resultSP = deliveryTimeService.calculate("SP", shippingDate);
        LocalDate resultMG = deliveryTimeService.calculate("MG", shippingDate);
        LocalDate resultRJ = deliveryTimeService.calculate("RJ", shippingDate);
        LocalDate resultES = deliveryTimeService.calculate("ES", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(5), resultSP);
        assertEquals(shippingDate.plusDays(5), resultMG);
        assertEquals(shippingDate.plusDays(5), resultRJ);
        assertEquals(shippingDate.plusDays(5), resultES);
    }

    @Test
    void calculate_SulState_Returns7Days() {
        // Act
        LocalDate resultPR = deliveryTimeService.calculate("PR", shippingDate);
        LocalDate resultRS = deliveryTimeService.calculate("RS", shippingDate);
        LocalDate resultSC = deliveryTimeService.calculate("SC", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(7), resultPR);
        assertEquals(shippingDate.plusDays(7), resultRS);
        assertEquals(shippingDate.plusDays(7), resultSC);
    }

    @Test
    void calculate_CentroOesteState_Returns8Days() {
        // Act
        LocalDate resultDF = deliveryTimeService.calculate("DF", shippingDate);
        LocalDate resultGO = deliveryTimeService.calculate("GO", shippingDate);
        LocalDate resultMT = deliveryTimeService.calculate("MT", shippingDate);
        LocalDate resultMS = deliveryTimeService.calculate("MS", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(8), resultDF);
        assertEquals(shippingDate.plusDays(8), resultGO);
        assertEquals(shippingDate.plusDays(8), resultMT);
        assertEquals(shippingDate.plusDays(8), resultMS);
    }

    @Test
    void calculate_NordesteState_Returns10Days() {
        // Act
        LocalDate resultAL = deliveryTimeService.calculate("AL", shippingDate);
        LocalDate resultBA = deliveryTimeService.calculate("BA", shippingDate);
        LocalDate resultCE = deliveryTimeService.calculate("CE", shippingDate);
        LocalDate resultMA = deliveryTimeService.calculate("MA", shippingDate);
        LocalDate resultPB = deliveryTimeService.calculate("PB", shippingDate);
        LocalDate resultPE = deliveryTimeService.calculate("PE", shippingDate);
        LocalDate resultPI = deliveryTimeService.calculate("PI", shippingDate);
        LocalDate resultRN = deliveryTimeService.calculate("RN", shippingDate);
        LocalDate resultSE = deliveryTimeService.calculate("SE", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(10), resultAL);
        assertEquals(shippingDate.plusDays(10), resultBA);
        assertEquals(shippingDate.plusDays(10), resultCE);
        assertEquals(shippingDate.plusDays(10), resultMA);
        assertEquals(shippingDate.plusDays(10), resultPB);
        assertEquals(shippingDate.plusDays(10), resultPE);
        assertEquals(shippingDate.plusDays(10), resultPI);
        assertEquals(shippingDate.plusDays(10), resultRN);
        assertEquals(shippingDate.plusDays(10), resultSE);
    }

    @Test
    void calculate_NorteState_Returns15Days() {
        // Act
        LocalDate resultAC = deliveryTimeService.calculate("AC", shippingDate);
        LocalDate resultAP = deliveryTimeService.calculate("AP", shippingDate);
        LocalDate resultAM = deliveryTimeService.calculate("AM", shippingDate);
        LocalDate resultPA = deliveryTimeService.calculate("PA", shippingDate);
        LocalDate resultRO = deliveryTimeService.calculate("RO", shippingDate);
        LocalDate resultRR = deliveryTimeService.calculate("RR", shippingDate);
        LocalDate resultTO = deliveryTimeService.calculate("TO", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(15), resultAC);
        assertEquals(shippingDate.plusDays(15), resultAP);
        assertEquals(shippingDate.plusDays(15), resultAM);
        assertEquals(shippingDate.plusDays(15), resultPA);
        assertEquals(shippingDate.plusDays(15), resultRO);
        assertEquals(shippingDate.plusDays(15), resultRR);
        assertEquals(shippingDate.plusDays(15), resultTO);
    }

    @Test
    void calculate_CaseInsensitiveState() {
        // Act
        LocalDate resultLowercase = deliveryTimeService.calculate("sp", shippingDate);
        LocalDate resultUppercase = deliveryTimeService.calculate("SP", shippingDate);
        LocalDate resultMixedCase = deliveryTimeService.calculate("Sp", shippingDate);

        // Assert
        assertEquals(shippingDate.plusDays(5), resultLowercase);
        assertEquals(shippingDate.plusDays(5), resultUppercase);
        assertEquals(shippingDate.plusDays(5), resultMixedCase);
    }
}