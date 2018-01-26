package com.digibig.service.person.domain;

public class Data{
    private String sex;
    private String address;
    private String birthday;

    public String getSex() {
      return sex;
    }

    public void setSex(String sex) {
      this.sex = sex;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getBirthday() {
      return birthday;
    }

    public void setBirthday(String birthday) {
      this.birthday = birthday;
    }

    @Override
    public String toString() {
      return "Data{" +
          "sex='" + sex + '\'' +
          ", address='" + address + '\'' +
          ", birthday='" + birthday + '\'' +
          '}';
    }
  }