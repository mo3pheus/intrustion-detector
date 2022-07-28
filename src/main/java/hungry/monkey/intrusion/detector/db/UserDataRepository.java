package hungry.monkey.intrusion.detector.db;

import hungry.monkey.intrusion.detector.domain.UserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDataRepository extends CrudRepository<UserData,Integer> {
    public Optional<UserData> findById(Integer id);

    @Query(value = "select * from user_data where user_name = ?1", nativeQuery = true)
    UserData findByUserName(String userName);
}
