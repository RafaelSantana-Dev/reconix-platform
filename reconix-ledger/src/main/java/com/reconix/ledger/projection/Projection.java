package com.reconix.ledger.projection;

import com.reconix.ledger.dto.EventDto;

public interface Projection<T> {
    void handle(EventDto event);

    T getCurrentState();

    void reset();
}