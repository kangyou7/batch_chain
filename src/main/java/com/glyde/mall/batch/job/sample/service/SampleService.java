package com.glyde.mall.batch.job.sample.service;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.glyde.mall.batch.job.sample.entity.PeopleDto;
import com.glyde.mall.batch.job.sample.mapper.PeopleMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SampleService {

	@Autowired
	@Qualifier("sqlSessionFactoryGlyde")
	SqlSessionFactory sqlSessionFactory;

	/**
	 * [기존 read(select)서비스사용] count
	 */
	int count = 0;

	public List<PeopleDto> getPeople() {
		log.debug("==================");
		/**
		 * [기존 read(select)서비스사용] Reader에서 기존 service(각 테이블 repository의 조합으로 처리되는 업무로직)을
		 * 그대로 사용할려면, 데이터를 읽은 마지막에는 null을 돌려줘야 중복실행되지 않는다. 여기 예제에서는 리턴값이
		 * List<PeopleDto>이다. 그래서 읽고 난 뒤에는 null로 반환한다. 리턴값이 List이기 때문에 writer에서 처리하는 값로
		 * List로 받아서 한꺼번에 처리한다. 대용량처리를 하는 일반 batch 의 경우는 PeopleDto(row) 단위로 읽어서
		 * reader.processor.writer과정을 거친다.
		 */
		count = count + 1;
		if (count > 1) {
			return null;
		}

		List<PeopleDto> l = sqlSessionFactory.openSession().getMapper(PeopleMapper.class).select();

		/**
		 * 데이터 변환(예시.processor를 안써도 여기 변환)
		 */
		for (PeopleDto p : l) {
			p.setLastName("OK");
		}

		return l;
	}

	public void updateAll(List<PeopleDto> l) {
		log.debug("==================");
		for (PeopleDto in : l) {
			sqlSessionFactory.openSession().getMapper(PeopleMapper.class).update(in);

		}
	}

	public void update(PeopleDto in) {
		log.debug("==================");
		sqlSessionFactory.openSession().getMapper(PeopleMapper.class).update(in);
	}
}
