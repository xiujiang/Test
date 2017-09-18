package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.Person;
import com.digibig.saaserp.person.domain.PersonExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PersonMapper {
    long countByExample(PersonExample example);

    int deleteByExample(PersonExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Person record);

    int insertSelective(Person record);

    List<Person> selectByExample(PersonExample example);

    Person selectByPrimaryKey(Integer id);
    
    Map<String , Object> getByCardNumber(String idCard);
    
    Map<String , Object> getPersonById(Integer id);
    
    Integer getdefaultMobile(Integer id);
    
    Integer getdefaultEmail(Integer id);
    
    Integer getdefaultAddress(Integer id);
    
    Integer getdefaultIDCard(Integer id);

    int updateByExampleSelective(@Param("record") Person record, @Param("example") PersonExample example);

    int updateByExample(@Param("record") Person record, @Param("example") PersonExample example);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
}