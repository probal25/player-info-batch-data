package ws.probal.batchdata.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobFinishedResponse {
    private String jobName;
    private String jobData;
    private String jobExecutionStartTime;
    private String jobExecutionEndTime;
    private String jobExecutionTime;
    private String jobStatus;
}
