package com.example.contact_eduardo_cardona_c0777368_android.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name="FIRST_NAME")
    private String firstName;
    @NonNull
    @ColumnInfo(name="LAST_NAME")
    private String lastName;
    @NonNull
    @ColumnInfo(name="PHONE_NUMBER")
    private String phoneNumber;
    @NonNull
    @ColumnInfo(name="EMAIL")
    private String email;
    @NonNull
    @ColumnInfo(name="ADDRESS")
    private String address;
    @NonNull
    @ColumnInfo(name="FULL_NAME")
    private String fullname;

    @NonNull
    public String getFullname() {
        return fullname;
    }

    public void setFullname(@NonNull String fullname) {
        this.fullname = fullname;
    }

    public Contact(@NonNull String firstName, @NonNull String lastName, @NonNull String phoneNumber, @NonNull String email, @NonNull String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.fullname = firstName + " " + lastName;
    }

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
        this.fullname = this.firstName  + " " + this.lastName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
        this.fullname = this.firstName  + " " + this.lastName;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }
}
