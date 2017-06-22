package com.otus.alexeenko.l8.services.datasets;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseDataSet{" +
                "id=" + id +
                '}';
    }
}