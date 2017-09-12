package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.Mobile;
import com.digibig.saaserp.person.domain.MobileExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MobileMapper {
    long countByExample(MobileExample example);

    int deleteByExample(MobileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Mobile record);

    int insertSelective(Mobile record);

    List<Mobile> selectByExample(MobileExample example);
    
    List<String> getMobiles(@Param("personId")Integer personId, @Param("enabled")Integer enabled);

    Mobile selectByPrimaryKey(Integer id);
    
    String getMobileById(Integer id);

    int updateByExampleSelective(@Param("record") Mobile record, @Param("example") MobileExample example);

    int updateByExample(@Param("record") Mobile record, @Param("example") MobileExample example);

    int updateByPrimaryKeySelective(Mobile record);

    int updateByPrimaryKey(Mobile record);
}