package com.github.kk.kafka.model;

import lombok.Data;

@Data
public class Signal {
    String id;
    String vehicle;
    Coords coords;
}
