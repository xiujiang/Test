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
import com.digibig.service.person.service.SchoolRecordService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("SchoolRecordController-e")
@RequestMapping("/v1.0/person/school/record")
public class SchoolRecordController {

  private Logger logger = LoggerFactory.getLogger(getClass());


  //学历id
  private static final String RECORDID = "recordId";

  //认证文件id
  private static final String RECORD_FILE = "reportFile";

  @Autowired
  private SchoolRecordService schoolRecordService;

  private com.digibig.service.person.controller.internal.SchoolRecordController schoolRecordController;

  @PostMapping("/add")
  public HttpResult<SchoolRecord> addSchoolRecord(@RequestBody SchoolRecord schoolRecord) {
    return schoolRecordController.addJson(schoolRecord);
  }

  @PostMapping("/update")
  public HttpResult<SchoolRecord> setSchoolRecord(@RequestBody SchoolRecord schoolRecord) {
    return schoolRecordController.updateSelectiveJson(schoolRecord);

  }

//
//  @PostMapping("/school/enabled")
//  public HttpResult<Boolean> setRecordEnabled(@RequestBody Map<String, String> paramMap) {
//
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)),
//        "设置学历证书信息的有效性personId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(RECORDID)), "recordId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
//
//    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
//    Integer recordId = Integer.valueOf(paramMap.get(RECORDID));
//    Enabled enabled = Enum
//        .valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
//
//    Boolean result = certificateService.setRecordEnabled(personId, recordId, enabled);
//
//    if (result) {
//      return new HttpResult<>(HttpStatus.OK, "成功", result);
//    }
//    return new HttpResult<>(HttpStatus.SERVER_ERROR, "失败", result);
//  }


  /**
   * <p> 查询自然人学历信息 </p>
   *
   * @param personId 自然人id
   * @return 学历信息
   */
  @GetMapping("/list")
  public HttpResult<List<SchoolRecord>> getSchoolRecord(@RequestParam("personId") Integer personId) {

    return new HttpResult<>(HttpStatus.OK, "成功", schoolRecordService.listWithParent(personId));
  }

}
