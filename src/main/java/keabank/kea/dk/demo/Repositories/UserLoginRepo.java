package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.UserLogin;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserLoginRepo extends CrudRepository<UserLogin,Long> {

    Optional<UserLogin>findByEmailAndPassword (String Email, String Password);
    UserLogin findByEmail(String Email);
    List<UserLogin> findAll();

    Optional<UserLogin> findByEmailAndSendserviceCode(String Email,String servicecode);


}
