package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.Bill;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface BillsRepo extends CrudRepository<Bill, Long> {
    Optional<Bill> findByObligatoryDigitsAndAccountNumberAndRegistrationNumber(String digits, Long account, Long reg);
    Optional<Bill> findByRegistrationNumberAndAccountNumber(Long reg, Long accountNumber);
}
