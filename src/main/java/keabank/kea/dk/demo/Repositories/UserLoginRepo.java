package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.UserLogin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserLoginRepo extends CrudRepository<UserLogin,Long> {

    Optional<UserLogin>findByEmailAndPassword (String Email, String Password);
    UserLogin findByEmail(String Email);
    UserLogin findByEmailAndAccountsList(String Email,String account);




}
