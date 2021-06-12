package com.example.contact_eduardo_cardona_c0777368_android.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.contact_eduardo_cardona_c0777368_android.model.Contact;
import com.example.contact_eduardo_cardona_c0777368_android.util.ECRoomDatabase;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;
    // repository constructor
    public ContactRepository(Application application) {
        ECRoomDatabase db = ECRoomDatabase.getInstance(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }
    // gets all the contacts
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
    // gets an specific contact
    public LiveData<Contact> getContact(int id) {
        return contactDao.getContact(id);
    }
    // creates a new contact
    public void insert(Contact contact) {
        ECRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.insertContact(contact));
    }
    // updates a contact
    public void update(Contact contact) {
        ECRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.update(contact));
    }
    // deletes a contact
    public void delete(Contact contact) {
        ECRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.delete(contact));
    }
}
