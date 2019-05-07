package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.Accounts;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Iaccountsrepository extends CrudRepository<Accounts,Long> {

List<Accounts> findAll();
   Accounts findAllByRegistrationnumberAndAccountNumber(Long reg,Long accnumber);
}
