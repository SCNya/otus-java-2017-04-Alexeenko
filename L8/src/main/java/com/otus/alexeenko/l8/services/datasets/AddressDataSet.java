package com.otus.alexeenko.l8.services.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    public String getStreet() {
        return street;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "street='" + street + '\'' +
                ", index=" + index +
                '}';
    }
}
