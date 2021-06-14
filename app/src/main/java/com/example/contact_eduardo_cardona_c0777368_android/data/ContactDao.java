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
    
    @Query("DELETE FROM contact WHERE ID = :id")
    void deleteContact(int id);

    @Query("UPDATE contact SET FIRST_NAME=:firstName, LAST_NAME=:lastName, PHONE_NUMBER=:phoneNumber, EMAIL=:email, ADDRESS=:address WHERE  ID = :id")
    void updateContact(int id, String firstName, String lastName, String phoneNumber, String email, String address);

    @Query("SELECT * FROM contact order by FULL_NAME ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contact WHERE ID = :id")
    LiveData<Contact> getContact( int id);

    @Query("Select * from contact where FULL_NAME LIKE '%' || :search || '%' or PHONE_NUMBER like '%' || :search || '%' or EMAIL like '%' || :search || '%' or ADDRESS like '%' || :search || '%'")
    LiveData<List<Contact>> getSearchedAllContacts(String search);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);
}
