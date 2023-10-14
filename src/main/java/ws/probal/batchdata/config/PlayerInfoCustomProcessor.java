package ws.probal.batchdata.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import ws.probal.batchdata.domain.dto.PlayerInfoMetadataDto;
import ws.probal.batchdata.domain.entity.PlayerInfo;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PlayerInfoCustomProcessor implements ItemProcessor<PlayerInfoMetadataDto, PlayerInfo> {
    @Override
    public PlayerInfo process(PlayerInfoMetadataDto item) throws Exception {
        log.info("Processing {}", item);
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setPlayerName(item.getName());
        playerInfo.setPlayerGender(item.getGender());
        playerInfo.setNation(item.getNation());
        playerInfo.setPosition(item.getPosition());
        playerInfo.setSkillMetadata(getSkillMetadata(item));
        return playerInfo;
    }

    private String getSkillMetadata(PlayerInfoMetadataDto item) {
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("overall", item.getOverall());
        jsonMap.put("pace", item.getPace());
        jsonMap.put("shooting", item.getShooting());
        jsonMap.put("passing", item.getPassing());
        jsonMap.put("dribbling", item.getDribbling());
        jsonMap.put("defending", item.getDefending());
        jsonMap.put("physicality", item.getPhysicality());
        jsonMap.put("acceleration", item.getAcceleration());
        jsonMap.put("sprint", item.getSprint());
        jsonMap.put("positioning", item.getPositioning());
        jsonMap.put("finishing", item.getFinishing());
        jsonMap.put("shotPower", item.getShot());
        jsonMap.put("longShots", item.getLongShots());
        jsonMap.put("volleys", item.getVolleys());
        jsonMap.put("penalties", item.getPenalties());
        jsonMap.put("vision", item.getVision());
        jsonMap.put("crossing", item.getCrossing());
        jsonMap.put("freeKickAccuracy", item.getFree());
        jsonMap.put("curve", item.getCurve());
        jsonMap.put("agility", item.getAgility());
        jsonMap.put("balance", item.getBalance());
        jsonMap.put("reactions", item.getReactions());
        jsonMap.put("ballControl", item.getBall());
        jsonMap.put("composure", item.getComposure());
        jsonMap.put("interceptions", item.getInterceptions());
        jsonMap.put("headingAccuracy", item.getHeading());
        jsonMap.put("defendingAwareness", item.getDef());
        jsonMap.put("standing", item.getStanding());
        jsonMap.put("sliding", item.getSliding());
        jsonMap.put("jumping", item.getJumping());
        jsonMap.put("stamina", item.getStamina());
        jsonMap.put("strength", item.getStrength());
        jsonMap.put("aggression", item.getAggression());
        jsonMap.put("attackingWorkRate", item.getAttWorkRate());
        jsonMap.put("defendingWorkRate", item.getDefWorkRate());
        jsonMap.put("preferredFoot", item.getPreferredFoot());
        jsonMap.put("weakFoot", item.getWeakFoot());
        jsonMap.put("skillMoves", item.getSkillMoves());
        jsonMap.put("url", item.getUrl());

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
}
