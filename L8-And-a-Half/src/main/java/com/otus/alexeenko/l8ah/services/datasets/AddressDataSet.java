package com.otus.alexeenko.l8ah.services.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by Vsevolod on 13/06/2017.
 */

@Entity
@Table(name = "addresses")
public class AddressDataSet extends BaseDataSet {

    @Column(name = "street")
    private String street;

    @Column(name = "index")
    private int index;

    public AddressDataSet() {
    }

    public AddressDataSet(long id, String street, int index) {
        super.setId(id);
        this.street = street;
        this.index = index;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getStreet() {
        return street;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDataSet that = (AddressDataSet) o;
        return index == that.index &&
                Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "street='" + street + '\'' +
                ", index=" + index +
                "} " + super.toString();
    }
}
