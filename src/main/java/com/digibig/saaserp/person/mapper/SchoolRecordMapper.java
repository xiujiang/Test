package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.SchoolRecord;
import com.digibig.saaserp.person.domain.SchoolRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SchoolRecordMapper {
    long countByExample(SchoolRecordExample example);

    int deleteByExample(SchoolRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SchoolRecord record);

    int insertSelective(SchoolRecord record);

    List<SchoolRecord> selectByExample(SchoolRecordExample example);

    SchoolRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SchoolRecord record, @Param("example") SchoolRecordExample example);

    int updateByExample(@Param("record") SchoolRecord record, @Param("example") SchoolRecordExample example);

    int updateByPrimaryKeySelective(SchoolRecord record);

    int updateByPrimaryKey(SchoolRecord record);
}