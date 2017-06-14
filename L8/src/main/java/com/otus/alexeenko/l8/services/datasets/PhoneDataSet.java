package com.otus.alexeenko.l8.services.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "phones")
public class PhoneDataSet extends BaseDataSet {

    @Column(name = "code")
    private int code;

    @Column(name = "number")
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(int code, String number) {
        this.code = code;
        this.number = number;
    }

    public int getCode() {
        return code;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "code=" + code +
                ", number='" + number + '\'' +
                '}';
    }
}
