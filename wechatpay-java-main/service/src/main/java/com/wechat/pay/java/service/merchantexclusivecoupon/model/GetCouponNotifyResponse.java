// Copyright 2021 Tencent Inc. All rights reserved.
//
// 营销商家券对外API
//
// No description provided (generated by Openapi Generator
// https://github.com/openapitools/openapi-generator)
//
// API version: 0.0.11

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.merchantexclusivecoupon.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** GetCouponNotifyResponse */
public class GetCouponNotifyResponse {
  /** 商户号 说明：商户号 */
  @SerializedName("mchid")
  private String mchid;

  /** 通知URL地址 说明：商户提供的用于接收商家券事件通知的URL地址，必须支持HTTPS。 */
  @SerializedName("notify_url")
  private String notifyUrl;

  public String getMchid() {
    return mchid;
  }

  public void setMchid(String mchid) {
    this.mchid = mchid;
  }

  public String getNotifyUrl() {
    return notifyUrl;
  }

  public void setNotifyUrl(String notifyUrl) {
    this.notifyUrl = notifyUrl;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCouponNotifyResponse {\n");
    sb.append("    mchid: ").append(toIndentedString(mchid)).append("\n");
    sb.append("    notifyUrl: ").append(toIndentedString(notifyUrl)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
