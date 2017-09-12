package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.CareerItem;
import com.digibig.saaserp.person.domain.CareerItemExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CareerItemMapper {
    long countByExample(CareerItemExample example);

    int deleteByExample(CareerItemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CareerItem record);

    int insertSelective(CareerItem record);

    List<CareerItem> selectByExampleWithBLOBs(CareerItemExample example);

    List<CareerItem> selectByExample(CareerItemExample example);
    
    List<Map<String ,String>> getItemsByCareerId(@Param("careerId") Integer careerId, @Param("enabled") Integer enabled);

    CareerItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CareerItem record, @Param("example") CareerItemExample example);

    int updateByExampleWithBLOBs(@Param("record") CareerItem record, @Param("example") CareerItemExample example);

    int updateByExample(@Param("record") CareerItem record, @Param("example") CareerItemExample example);

    int updateByPrimaryKeySelective(CareerItem record);

    int updateByPrimaryKeyWithBLOBs(CareerItem record);

    int updateByPrimaryKey(CareerItem record);
}