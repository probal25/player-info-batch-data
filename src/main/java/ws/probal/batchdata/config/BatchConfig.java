package ws.probal.batchdata.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import ws.probal.batchdata.domain.dto.PlayerInfoMetadataDto;
import ws.probal.batchdata.domain.entity.PlayerInfo;
import ws.probal.batchdata.repository.PlayerInfoRepository;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    @Bean(name = "playerMetadataBachJob")
    public Job playerMetadataBachJob(JobRepository jobRepository, @Qualifier("readDataStep") Step readDataStep) {
        return new JobBuilder("playerMetadataBachJob", jobRepository)
                .preventRestart()
                .start(readDataStep)
                .build();
    }

    @Bean
    protected Step readDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                PlayerInfoRepository playerInfoRepository) {
        return new StepBuilder("readDataStep", jobRepository)
                .<PlayerInfoMetadataDto, PlayerInfo>chunk(10, transactionManager)
                .reader(playerInfoItemReader())
                .processor(playerInfoItemProcessor())
                .writer(playerInfoItemWriter(playerInfoRepository))
                .build();
    }

    @Bean
    public ItemReader<PlayerInfoMetadataDto> playerInfoItemReader() {
        FlatFileItemReader<PlayerInfoMetadataDto> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("data/players_metadata.csv"));
        itemReader.setName("PLAYER METADATA READER");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public ItemProcessor<PlayerInfoMetadataDto, PlayerInfo> playerInfoItemProcessor() {
        return new PlayerInfoCustomProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<PlayerInfo> playerInfoItemWriter(PlayerInfoRepository playerInfoRepository) {
        return new PlayerInfoCustomWriter(playerInfoRepository);
    }

    private LineMapper<PlayerInfoMetadataDto> lineMapper() {
        DefaultLineMapper<PlayerInfoMetadataDto> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(getLineTokenizer());
        lineMapper.setFieldSetMapper(getFieldSetMapper());
        return lineMapper;
    }

    private DelimitedLineTokenizer getLineTokenizer() {
        String[] tokens = {
                "Id", "Name", "Nation", "Club", "Position", "Age", "Overall", "Pace", "Shooting",
                "Passing", "Dribbling", "Defending", "Physicality", "Acceleration", "Sprint",
                "Positioning", "Finishing", "Shot", "LongShot", "Volleys", "Penalties", "Vision",
                "Crossing", "Free", "Curve", "Agility", "Balance", "Reactions", "Ball",
                "Composure", "Interceptions", "Heading", "Def", "Standing", "Sliding",
                "Jumping", "Stamina", "Strength", "Aggression", "Att work rate", "Def work rate",
                "Preferred foot", "Weak foot", "Skill moves", "URL", "Gender", "GK"
        };

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(tokens);

        return lineTokenizer;
    }

    private FieldSetMapper<PlayerInfoMetadataDto> getFieldSetMapper() {
        BeanWrapperFieldSetMapper<PlayerInfoMetadataDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PlayerInfoMetadataDto.class);
        return fieldSetMapper;
    }
}
