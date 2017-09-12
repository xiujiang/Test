package com.digibig.saaserp.person.mapper;

import com.digibig.saaserp.person.domain.DiplomaCertificate;
import com.digibig.saaserp.person.domain.DiplomaCertificateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DiplomaCertificateMapper {
    long countByExample(DiplomaCertificateExample example);

    int deleteByExample(DiplomaCertificateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DiplomaCertificate record);

    int insertSelective(DiplomaCertificate record);

    List<DiplomaCertificate> selectByExample(DiplomaCertificateExample example);

    DiplomaCertificate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DiplomaCertificate record, @Param("example") DiplomaCertificateExample example);

    int updateByExample(@Param("record") DiplomaCertificate record, @Param("example") DiplomaCertificateExample example);

    int updateByPrimaryKeySelective(DiplomaCertificate record);

    int updateByPrimaryKey(DiplomaCertificate record);
}