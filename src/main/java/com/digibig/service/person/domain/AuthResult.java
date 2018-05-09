package com.digibig.service.person.domain;

public class AuthResult {

  private Resp resp;

  private Data data;

  public Resp getResp() {
    return resp;
  }

  public void setResp(Resp resp) {
    this.resp = resp;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "AuthResult{" +
        "resp=" + resp +
        ", data=" + data +
        '}';
  }
}
