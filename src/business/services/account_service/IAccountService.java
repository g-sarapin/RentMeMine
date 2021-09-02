package business.services.account_service;

import business.documents.Person;
import org.springframework.transaction.annotation.Transactional;

public interface IAccountService {
    Person getPerson(String id, String secId, boolean blkChk, boolean maybeNew);

    @Transactional
    Person savePerson(Person person);

    void removePerson(String id);
}