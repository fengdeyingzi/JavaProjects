// Copyright 2021 Tencent Inc. All rights reserved.
//
// 微信支付分停车服务
//
// 微信支付分停车服务 扣费API
//
// API version: 1.2.1

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.wexinpayscoreparking.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** PlateService */
public class PlateService {
  /** 车牌号 说明：车牌号，仅包括省份+车牌，不包括特殊字符。 */
  @SerializedName("plate_number")
  private String plateNumber;

  /** 车牌颜色 说明：车牌颜色 */
  @SerializedName("plate_color")
  private PlateColor plateColor;

  /**
   * 车牌服务开通时间
   * 说明：车牌服务开通时间，遵循[rfc3339](https://datatracker.ietf.org/doc/html/rfc3339)标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
   * 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
   */
  @SerializedName("service_open_time")
  private String serviceOpenTime;

  /** 用户标识 说明：用户在商户对应appid下的唯一标识，此处返回商户请求中的openid */
  @SerializedName("openid")
  private String openid;

  /**
   * 车牌服务开通状态 说明：车牌服务开通状态， NORMAL 正常服务 PAUSE 暂停服务 OUT_SERVICE 未开通 商户根据状态带用户跳转至对应的微信支付分停车服务小程序页面。
   * 其中NORMAL 和 PAUSE状态，可跳转至车牌管理页，进行车牌服务状态管理。OUT_SERVICE状态，可跳转至服务开通页面。
   */
  @SerializedName("service_state")
  private String serviceState;

  public String getPlateNumber() {
    return plateNumber;
  }

  public void setPlateNumber(String plateNumber) {
    this.plateNumber = plateNumber;
  }

  public PlateColor getPlateColor() {
    return plateColor;
  }

  public void setPlateColor(PlateColor plateColor) {
    this.plateColor = plateColor;
  }

  public String getServiceOpenTime() {
    return serviceOpenTime;
  }

  public void setServiceOpenTime(String serviceOpenTime) {
    this.serviceOpenTime = serviceOpenTime;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getServiceState() {
    return serviceState;
  }

  public void setServiceState(String serviceState) {
    this.serviceState = serviceState;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlateService {\n");
    sb.append("    plateNumber: ").append(toIndentedString(plateNumber)).append("\n");
    sb.append("    plateColor: ").append(toIndentedString(plateColor)).append("\n");
    sb.append("    serviceOpenTime: ").append(toIndentedString(serviceOpenTime)).append("\n");
    sb.append("    openid: ").append(toIndentedString(openid)).append("\n");
    sb.append("    serviceState: ").append(toIndentedString(serviceState)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
