package ws.probal.batchdata.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.observability.BatchMetrics;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.probal.batchdata.domain.response.JobFinishedResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/run-job")
public class JobController {

    private final JobLauncher jobLauncher;
    private final Job job;
    private JobExecution jobExecution;

    @GetMapping("/import-player-metadata")
    public ResponseEntity<JobFinishedResponse> importPlayerMetadata() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("started-at:", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobExecution = jobLauncher.run(job, jobParameters);
            JobFinishedResponse response = prepareResponseBody(jobExecution);
            return ResponseEntity.ok(response);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
            return ResponseEntity.ok(prepareResponseBody(jobExecution));
        }
    }

    private JobFinishedResponse prepareResponseBody(JobExecution jobExecution) {
        return JobFinishedResponse
                .builder()
                .jobName(jobExecution.getJobInstance().getJobName())
                .jobData(jobExecution.getStepExecutions().stream().toList().toString())
                .jobExecutionStartTime(formatLocalDate(jobExecution.getCreateTime()))
                .jobExecutionEndTime(formatLocalDate(Objects.requireNonNull(jobExecution.getEndTime())))
                .jobExecutionTime(getJobExecutionTime())
                .jobStatus(jobExecution.getStatus().name())
                .build();
    }

    private String formatLocalDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String getJobExecutionTime() {
        Duration duration = BatchMetrics.calculateDuration(jobExecution.getStartTime(),
                jobExecution.getEndTime());
        return BatchMetrics.formatDuration(duration);
    }
}
