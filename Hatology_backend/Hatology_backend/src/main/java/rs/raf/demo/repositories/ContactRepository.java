package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Contact;
import rs.raf.demo.model.Orders;
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

}
