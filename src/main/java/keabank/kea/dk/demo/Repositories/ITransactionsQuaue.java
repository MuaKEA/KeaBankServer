package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.TransactionsQuaue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITransactionsQuaue extends JpaRepository<TransactionsQuaue,Long> {

    List<TransactionsQuaue> findByDateBeforeOrDate(LocalDate before,LocalDate now);
    List<TransactionsQuaue> findByDate(LocalDate date);


}
