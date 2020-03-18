package com.glyde.mall.batch.job.sample;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.glyde.mall.batch.job.sample.entity.PeopleDto;
import com.glyde.mall.batch.job.sample.service.SampleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SampleServiceItemJobConfig {

	@Autowired
	@Qualifier("sqlSessionFactoryGlyde")
	SqlSessionFactory sqlSessionFactory;

	@Autowired
	private JobParametersIncrementer incrementer;

	@Autowired
	private JobExecutionListener jobListener;

	@Autowired
	private StepExecutionListener stepListener;

	@Autowired
	JobBuilderFactory jobs;

	@Autowired
	StepBuilderFactory steps;

	private static final int CHUNK_SIZE = 1;

	/**
	 * Sample job job. Program argument 추가 후 BTStarter.exe 로 서비스 실행
	 * --spring.batch.job.names=com.glyde.mall.batch.job.sample.sampleJob.sampleJob
	 * --executor.name="BATCH_EXECUTOR"
	 *
	 * select row 단위로 read,write한 경우
	 * @return the job
	 * @throws Exception the exception
	 */
	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceItemJob")
	public Job sampleJob() throws Exception {
		return jobs.get("com.glyde.mall.batch.job.sample.sampleServiceItemJob").start(sampleStep())
				.incrementer(incrementer).listener(jobListener).build();
	}

	/**
	 * Sample step step.
	 *
	 * @return the step
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceItemStep")
	@JobScope
	public Step sampleStep() throws Exception {
		return steps.get("com.glyde.mall.batch.job.sample.sampleServiceItemStep").listener(stepListener)
				.allowStartIfComplete(true).<PeopleDto, PeopleDto>chunk(CHUNK_SIZE).reader(sampleReader())
				.writer(sampleWriter()).build();
	}

	/**
	 * Sample reader item reader.
	 *
	 * @return the item reader
	 * @throws Exception the exception
	 */
	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceItemReader")
	@StepScope
	public MyBatisCursorItemReader<PeopleDto> sampleReader() throws Exception {
		return new MyBatisCursorItemReaderBuilder<PeopleDto>().sqlSessionFactory(sqlSessionFactory)
				.queryId("com.glyde.mall.batch.job.sample.mapper.PeopleMapper.select").build();
	}

	@Autowired
	SampleService sampleService;
	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceItemWriter")
	@StepScope
	public ItemWriterAdapter<PeopleDto> sampleWriter() throws Exception {
		ItemWriterAdapter<PeopleDto> i = new ItemWriterAdapter<PeopleDto>();
		i.setTargetObject(sampleService);
		i.setTargetMethod("update");

		return i;
	}

}
