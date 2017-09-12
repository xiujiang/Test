package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.EducationSummary;
import com.digibig.saaserp.person.domain.EducationSummaryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EducationSummaryMapper {
    long countByExample(EducationSummaryExample example);

    int deleteByExample(EducationSummaryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EducationSummary record);

    int insertSelective(EducationSummary record);

    List<EducationSummary> selectByExample(EducationSummaryExample example);

    EducationSummary selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EducationSummary record, @Param("example") EducationSummaryExample example);

    int updateByExample(@Param("record") EducationSummary record, @Param("example") EducationSummaryExample example);

    int updateByPrimaryKeySelective(EducationSummary record);

    int updateByPrimaryKey(EducationSummary record);
}