package com.example.contact_eduardo_cardona_c0777368_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contact_eduardo_cardona_c0777368_android.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    void insertContact(Contact contact);

    @Query("DELETE FROM contact")
    void deleteAll();

    @Query("DELETE FROM contact WHERE id = :id")
    void deleteContact(int id);

    @Query("UPDATE contact SET first_name=:firstName, last_name=:lastName, phone_number=:phoneNumber, email=:email, address=:address WHERE  id = :id")
    void updateContact(int id, String firstName, String lastName, String phoneNumber, String email, String address);

    @Query("SELECT * FROM contact order by first_name ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contact WHERE id = :id order by first_name ASC")
    LiveData<Contact> getContact( int id);


    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);
}
