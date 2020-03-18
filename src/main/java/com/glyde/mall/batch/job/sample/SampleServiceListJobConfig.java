package com.glyde.mall.batch.job.sample;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
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
public class SampleServiceListJobConfig {

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
	 * select List로 read,write한 경우
	 * @return the job
	 * @throws Exception the exception
	 */
	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceListJob")
	public Job sampleJob() throws Exception {
		return jobs.get("com.glyde.mall.batch.job.sample.sampleServiceListJob").start(sampleStep()).incrementer(incrementer)
				.listener(jobListener).build();
	}

	/**
	 * Sample step step.
	 *
	 * @return the step
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceListStep")
	@JobScope
	public Step sampleStep() throws Exception {
		return steps.get("com.glyde.mall.batch.job.sample.sampleServiceListStep").listener(stepListener)
				.allowStartIfComplete(true).<PeopleDto, PeopleDto>chunk(CHUNK_SIZE).reader(sampleReader(null))
				
				.writer((ItemWriter<? super PeopleDto>) sampleWriter(null)).build();
	}

	/**
	 * Sample reader item reader.
	 *
	 * @return the item reader
	 * @throws Exception the exception
	 */
	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceListReader")
	@StepScope
	public ItemReader<? extends PeopleDto> sampleReader(SampleService sampleServiceList) throws Exception {
		ItemReaderAdapter<List<PeopleDto>> i = new ItemReaderAdapter<List<PeopleDto>>();
		i.setTargetObject(sampleServiceList);
		i.setTargetMethod("getPeople");
		return (ItemReader<? extends PeopleDto>) i;
	}
	
	

	@Bean(name = "com.glyde.mall.batch.job.sample.sampleServiceListWriter")
	@StepScope
	public ItemWriter<? extends PeopleDto> sampleWriter(SampleService sampleServiceList) throws Exception {
		ItemWriterAdapter<List<PeopleDto>> i = new ItemWriterAdapter<List<PeopleDto>>();
		i.setTargetObject(sampleServiceList);
		i.setTargetMethod("updateAll");

		return (ItemWriter<? extends PeopleDto>) i;
	}

}
