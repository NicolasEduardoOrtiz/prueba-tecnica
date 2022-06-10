package com.ayesa.pruebatecnica.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public final class Transaction {

    private final Integer id;
    private final String merchant;
    private final Integer amount;
    private final LocalDateTime time;
    
}
