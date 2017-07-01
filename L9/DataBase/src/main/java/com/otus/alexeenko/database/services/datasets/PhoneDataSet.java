package com.otus.alexeenko.database.services.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "phones")
public class PhoneDataSet extends BaseDataSet {

    @Column(name = "code")
    private int code;

    @Column(name = "number")
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(long id, int code, String number) {
        super.setId(id);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return code == that.code &&
                Objects.equals(number, that.number);
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "code=" + code +
                ", number='" + number + '\'' +
                "} " + super.toString();
    }
}
