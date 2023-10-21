package ws.probal.batchdata.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import ws.probal.batchdata.domain.entity.PlayerInfo;
import ws.probal.batchdata.repository.PlayerInfoRepository;


@Slf4j
@RequiredArgsConstructor
public class PlayerInfoCustomWriter implements ItemWriter<PlayerInfo> {

    private final PlayerInfoRepository repository;

    @Override
    public void write(Chunk<? extends PlayerInfo> chunk) throws Exception {

        log.info("Writing chunk of: {}", chunk.size());
        chunk.getItems().forEach(
                playerInfo -> log.info("Inserting player :::: {}", playerInfo.getPlayerName())
        );
        repository.saveAll(chunk.getItems());
    }
}