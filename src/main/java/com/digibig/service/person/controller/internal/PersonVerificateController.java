package com.digibig.service.person.controller.internal;

import com.digibig.commons.api.HttpResult;
import com.digibig.commons.constant.Charset;
import com.digibig.commons.constant.HttpMethod;
import com.digibig.commons.exception.DigibigException;
import com.digibig.commons.util.HttpClient;
import com.digibig.service.person.domain.Person;
import com.digibig.service.person.domain.SchoolRecord;
import com.digibig.service.person.service.AddressService;
import com.digibig.service.person.service.PersonService;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import com.digibig.spring.credential.CredentialHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "internal/v1.0/person/verificate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Domain(SchoolRecord.class)
@Qualifier("internal")
@NoLogin
public class PersonVerificateController {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  protected ObjectMapper mapper;

  @Autowired
  private CredentialHelper credentialHelper;

  @Autowired
  private PersonService personService;

  //授权有效时间
  private static final int EXPIRE_TIME = 120;

  //默认超时时间
  private static final Integer DEFAULT_TIMEOUT = 60 * 1000;
  //默认编码
  private static final String DEFAULT_CHARSET = Charset.UTF_8;
  //身份验证匹配时返回的code
  private static final String SUCCESS_CODE = "0";
  //第三方身份验证匹配时的url
  private static final String DEAFULT_URL = "http://idcard.market.alicloudapi.com/lianzhuo/idcard?";

  //第三方身份验证匹配时的appCode
  @Value("${person.appCode}")
  private String appCode;




  private Boolean checkCardnoAndName(String cardno, String name) throws DigibigException {
    logger.info("身份核实--第三方认证");
    Map<String, String> headers = new HashMap<>();
    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
    headers.put("Authorization", "APPCODE " + appCode);

    StringBuilder sb = new StringBuilder(DEAFULT_URL)
        .append("cardno=")
        .append(cardno)
        .append("&name=")
        .append(name);

    HttpResult<String> result = HttpClient
        .execute(sb.toString(), HttpMethod.GET, headers, DEFAULT_CHARSET, null, DEFAULT_TIMEOUT);

    String code = null;
//
//    JsonNode node = mapper.readTree(result.getData());
//    if (Objects.isNull(result) ) {
//      code = JSON.parseObject(
//          JSON.parseObject(result.getData())
//              .get("resp").toString())
//          .get("code").toString();
//    }
    return SUCCESS_CODE.equals(code);
  }


  /*
   * 获取授权
   */
  private String getCredential() {
    return credentialHelper.issue(credentialHelper.newCredential(), EXPIRE_TIME);
  }

  /*
   * 身份认证
   */
  public Map<String, String> identityVerificate(String idCard, String name)
      throws DigibigException {
    String credential = this.getCredential();
    //根据身份证号和姓名查询自然人
    Person example = new Person();
    example.setIdNumber(idCard);
    example.setName(name);

    List<Person> idCards = personService.queryAll(example);

    Map<String, String> mapResult = new HashMap<>();
    Boolean result = false;

    //数据库中有该自然人，返回自然人id和授权信息
    if (!CollectionUtils.isEmpty(idCards)) {
      mapResult.put("personId", idCards.get(0).getId().toString());
      mapResult.put("credential", credential);
      return mapResult;
    } else {
      //数据库中没有，则调用第三方接口查询
      result = checkCardnoAndName(idCard, name);
    }

    //合法，在数据库中添加自然对象
    if (result) {
      Person person = new Person();
      person.setName(name);
      person.setIdNumber(idCard);

      personService.add(person);

      mapResult.put("personId", person.getId().toString());
      mapResult.put("credential", credential);
      return mapResult;
    }
    //不合法，返回null
    return null;
  }
}
