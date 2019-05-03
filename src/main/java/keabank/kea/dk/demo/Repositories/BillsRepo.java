package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.Bills;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface BillsRepo extends CrudRepository<Bills, Long> {
    Optional<Bills> findByObligatoryDigitsAndAccountNumberAndRegistrationNumber(String digits, Long account, Long reg);
}
