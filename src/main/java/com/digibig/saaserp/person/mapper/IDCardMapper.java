package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.IDCard;
import com.digibig.saaserp.person.domain.IDCardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IDCardMapper {
    long countByExample(IDCardExample example);

    int deleteByExample(IDCardExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(IDCard record);

    int insertSelective(IDCard record);

    List<IDCard> selectByExample(IDCardExample example);

    IDCard selectByPrimaryKey(Integer id);
    
    String getIDCardById(Integer id);

    int updateByExampleSelective(@Param("record") IDCard record, @Param("example") IDCardExample example);

    int updateByExample(@Param("record") IDCard record, @Param("example") IDCardExample example);

    int updateByPrimaryKeySelective(IDCard record);

    int updateByPrimaryKey(IDCard record);
}