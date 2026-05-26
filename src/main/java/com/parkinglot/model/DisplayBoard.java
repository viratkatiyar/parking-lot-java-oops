package com.parkinglot.model;

import java.util.HashMap;
import java.util.Map;

public class DisplayBoard {
    private final String boardId;
    private final Map<String, Integer> freeSpotCounts;

    public DisplayBoard(String boardId) {
        this.boardId = boardId;
        this.freeSpotCounts = new HashMap<>();
    }

    public String getBoardId() {
        return boardId;
    }

    public void update(String spotType, int count) {
        freeSpotCounts.put(spotType, count);
    }

    public void printDisplay() {
        System.out.println("======================================");
        System.out.println("   DISPLAY BOARD: " + boardId);
        System.out.println("======================================");
        if (freeSpotCounts.isEmpty()) {
            System.out.println("   No spot data available.");
        } else {
            for (Map.Entry<String, Integer> entry : freeSpotCounts.entrySet()) {
                String typeName = entry.getKey();
                int count = entry.getValue();
                String status = count == 0 ? "FULL" : String.valueOf(count);
                System.out.printf("   %-15s : %s\n", typeName, status);
            }
        }
        System.out.println("======================================");
    }
}
