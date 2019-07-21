package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.Accounts;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface Iaccountsrepository extends CrudRepository<Accounts,Long> {

List<Accounts> findAll();
   Optional<Accounts> findAllByRegistrationnumberAndAccountNumber(Long reg, Long accnumber);
}
