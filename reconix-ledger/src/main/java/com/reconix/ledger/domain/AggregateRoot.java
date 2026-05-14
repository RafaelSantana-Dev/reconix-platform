package com.reconix.ledger.domain;

import com.reconix.ledger.dto.EventDto;

import java.util.List;

public abstract class AggregateRoot {
    protected final String aggregateId;
    protected int version;

    public AggregateRoot(String aggregateId) {
        this.aggregateId = aggregateId;
        this.version = 0;
    }

    public abstract void apply(EventDto event);

    public String getAggregateId() {
        return aggregateId;
    }

    public int getVersion() {
        return version;
    }

    protected void incrementVersion() {
        this.version++;
    }
}