package com.digibig.service.person.service;


import com.digibig.commons.constant.Charset;
import com.digibig.service.person.domain.AuthResult;
import com.digibig.service.person.remote.AuthorizationRemote;
import com.digibig.service.person.domain.Person;
import com.digibig.spring.credential.CredentialHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class PersonVerificateService {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  protected ObjectMapper mapper;

  @Autowired
  private CredentialHelper credentialHelper;

  @Autowired
  private PersonService personService;

  @Autowired
  private AuthorizationRemote authorizationRemote;

  //授权有效时间
  private static final int EXPIRE_TIME = 120;

  //第三方身份验证匹配时的appCode
  @Value("${person.appCode}")
  private String appCode;

  private void checkCardnoAndName(String cardno, String name) {
    AuthResult result = authorizationRemote.auth(cardno,name,"APPCODE ".concat(appCode));
    if(Objects.isNull(result) || Objects.isNull(result.getResp()) || Objects.isNull(result.getResp().getCode())){
      throw new RuntimeException("调用第三方实名认证失败。");
    }
    if(0 != result.getResp().getCode()){
      throw new RuntimeException("调用第三方实名认证失败。");
    }
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
  public Person identityVerificate(Person example) {

    String credential = this.getCredential();

    //根据身份证号和姓名查询自然人
    List<Person> persons = personService.queryAll(example);
//    Person person = personService.queryFirst(example);

    if (!CollectionUtils.isEmpty(persons)) {
      return persons.get(0);
    }

    this.checkCardnoAndName(example.getIdNumber(), example.getName());

//    personService.add(example);
//    example.setCredential(credential);
    return personService.add(example);
  }
}
