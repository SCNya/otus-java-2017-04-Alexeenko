package com.otus.alexeenko.database.services.datasets;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public BaseDataSet() {
    }

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