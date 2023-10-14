package ws.probal.batchdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ws.probal.batchdata.domain.entity.PlayerInfo;

public interface PlayerInfoRepository extends JpaRepository<PlayerInfo, Long> {
}
