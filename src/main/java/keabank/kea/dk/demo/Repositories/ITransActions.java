package keabank.kea.dk.demo.Repositories;

import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.TransactionsQuaue;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ITransActions extends CrudRepository<TransActions,Long> {
    List<TransactionsQuaue> findByDateBeforeOrDate(LocalDate now, LocalDate now1);
}
