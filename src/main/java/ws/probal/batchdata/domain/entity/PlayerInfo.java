package ws.probal.batchdata.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PLAYER_INFO")
public class PlayerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String playerName;
    private String playerGender;
    private String nation;
    private String position;
    @Column(name = "SKILL_META_DATA", columnDefinition = "TEXT")
    private String skillMetadata;
}
