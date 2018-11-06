package be.kdg.processor.shared.livestats;

import lombok.Data;

@Data
public class LiveStats {
    private int totalMessages;
    private int totalFines;
    private double averageFinePrice;
}
