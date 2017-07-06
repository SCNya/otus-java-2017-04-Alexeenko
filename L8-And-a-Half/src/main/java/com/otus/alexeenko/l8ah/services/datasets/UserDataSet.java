package com.otus.alexeenko.l8ah.services.datasets;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserDataSet extends BaseDataSet {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToMany
    @JoinTable(name = "phones",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    private List<PhoneDataSet> phones;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;

    //Important for Hibernate
    public UserDataSet() {
    }

    public UserDataSet(long id, String name, int age, List<PhoneDataSet> phones, AddressDataSet address) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataSet that = (UserDataSet) o;
        return age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(phones, that.phones) &&
                Objects.equals(address, that.address);
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
