package com.digibig.service.person.domain;

public class Resp{
    private Integer code;
    private String desc;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    @Override
    public String toString() {
      return "Resp{" +
          "code='" + code + '\'' +
          ", desc='" + desc + '\'' +
          '}';
    }
  }