package com.otus.alexeenko.l8.services.datasets;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "users")
public class UserDataSet extends BaseDataSet {

    @Column(name = "name")
    private String name;

    @Column(name = "age", columnDefinition = "int default 0")
    private int age;

    @OneToMany
    @JoinTable(name = "phones",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    private List<PhoneDataSet> phones;

    @OneToOne(cascade = CascadeType.ALL)
    @Column(name = "address")
    private AddressDataSet address;

    //Important for Hibernate
    public UserDataSet() {
    }

    public UserDataSet(BigInteger id, String name, int age, List<PhoneDataSet> phones, AddressDataSet address) {
        super.setId(id);
        this.name = name;
        this.age = age;
        this.phones = phones;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", phones=" + phones +
                ", address=" + address +
                "} " + super.toString();
    }
}
