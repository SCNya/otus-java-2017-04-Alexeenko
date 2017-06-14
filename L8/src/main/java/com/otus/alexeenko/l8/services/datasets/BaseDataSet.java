package com.otus.alexeenko.l8.services.datasets;

import javax.persistence.*;
import java.math.BigInteger;

@MappedSuperclass
public abstract class BaseDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseDataSet{" +
                "id=" + id +
                '}';
    }
}