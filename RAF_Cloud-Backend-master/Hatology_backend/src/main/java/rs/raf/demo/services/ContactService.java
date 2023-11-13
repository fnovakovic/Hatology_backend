package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Contact;
import rs.raf.demo.repositories.*;

import java.util.List;

@Service
public class ContactService {
    private ContactRepository contactRepository;
    private ContactCrudRepository contactCrudRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository,ContactCrudRepository contactCrudRepository) {
        this.contactRepository = contactRepository;
        this.contactCrudRepository = contactCrudRepository;

    }

    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }




    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }


    public void deleteByUserId(Long id) {
         contactCrudRepository.delete(id);
    }





}