/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.SchoolRecord;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.auth.Domain;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("SchoolRecordController-e")
@RequestMapping("external/v1.0/person/school/record")
@Domain(SchoolRecord.class)
@Qualifier("external")
public class SchoolRecordController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  @Qualifier("internal")
  private com.digibig.service.person.controller.internal.SchoolRecordController schoolRecordController;

  @PostMapping("/add")
  public HttpResult<SchoolRecord> addSchoolRecord(@RequestBody SchoolRecord schoolRecord) {
    return schoolRecordController.addJson(schoolRecord);
  }

  @PostMapping("/update")
  public HttpResult<SchoolRecord> setSchoolRecord(@RequestBody SchoolRecord schoolRecord) {
    return schoolRecordController.updateSelectiveJson(schoolRecord);

  }

  /**
   * <p> 查询自然人学历信息 </p>
   *
   * @param personId 自然人id
   * @return 学历信息
   */
  @GetMapping("/list")
  public HttpResult<Collection<SchoolRecord>> getSchoolRecord(@RequestParam("personId") Integer personId) {

    return schoolRecordController.list(null, null, null, null, personId, null);
  }

}
