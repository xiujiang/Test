package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.Career;
import com.digibig.saaserp.person.domain.CareerExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CareerMapper {
  
    long countByExample(CareerExample example);

    int deleteByExample(CareerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Career record);

    int insertSelective(Career record);

    List<Career> selectByExample(CareerExample example);
    
    List<Map<String,Object>> getCareersByPersonId(@Param("personId") Integer personId, @Param("enabled") Integer enabled);

    Career selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Career record, @Param("example") CareerExample example);

    int updateByExample(@Param("record") Career record, @Param("example") CareerExample example);

    int updateByPrimaryKeySelective(Career record);

    int updateByPrimaryKey(Career record);
}