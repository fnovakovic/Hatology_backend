package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.User;

@Repository
public interface UserCrudRepository extends CrudRepository<User, Long> {
}
