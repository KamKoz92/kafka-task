package com.github.kk.kafka.model;

import lombok.Data;

@Data
public class Coords {
    double x;
    double y;

    public static double calculateDistance(Coords c1, Coords c2) {
        return Math.sqrt(Math.pow(c2.getX() - c1.getX(), 2) + Math.pow(c2.getY() - c1.getY(), 2));
    }
}
