package hungry.monkey.intrusion.detector.db;

import hungry.monkey.intrusion.detector.domain.IpData;
import hungry.monkey.intrusion.detector.domain.UserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IpDataRepository extends CrudRepository<IpData, Integer> {
    public Optional<IpData> findById(Integer id);

    @Query(value = "select * from ip_data where ip_address = ?1", nativeQuery = true)
    IpData findByIpAddress(String ip);
}
