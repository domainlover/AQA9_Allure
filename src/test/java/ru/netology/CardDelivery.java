package ru.netology;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CardDelivery {
    private final String city;
    private final String name;
    private final String phone;
}