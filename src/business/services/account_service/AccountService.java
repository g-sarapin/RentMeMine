package business.services.account_service;

import business.documents.Person;
import business.exceptions.DataBaseException;
import business.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    PersonRepository repo;

    @Override
    public Person getPerson(String id, String secId, boolean blkChk, boolean maybeNew){
        if(secId != null && !secId.equals(id))
            throw new SecurityException("Recipient is not an owner of this profile, access denied!");
        Person person = repo.findById(id).orElse(null);
        if (person == null){
            if (maybeNew) return null;
            else throw new DataBaseException("Profile " + id + " not found!");
        }
        if(blkChk && person.isBlocked()) throw  new SecurityException("Profile " + id + " is blocked!");
        return person;
    }

    @Override
    @Transactional
    public Person savePerson(Person person){
        return repo.save(person);
    }

    @Override
    public void removePerson(String id){
        repo.deleteById(id);
    }
}