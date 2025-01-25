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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** QueryCouponRequest */
public class QueryCouponRequest {
  /** 券code 说明：券的唯一标识 */
  @SerializedName("coupon_code")
  @Expose(serialize = false)
  private String couponCode;

  /**
   * 公众账号ID 说明：支持传入与当前调用接口商户号有绑定关系的AppID。支持小程序AppID与公众号AppID。
   * 校验规则：传入的AppID得是与调用方商户号（即请求头里面的商户号）有绑定关系的AppID或传入的AppID得是归属商户号有绑定关系的AppID
   */
  @SerializedName("appid")
  @Expose(serialize = false)
  private String appid;

  /**
   * 用户OpenID 说明：OpenID信息，用户在AppID下的唯一标识。
   * 校验规则：传入的OpenID得是调用方商户号（即请求头里面的商户号）有绑定关系的AppID获取的OpenID或传入的OpenID得是归属商户号有绑定关系的AppID获取的OpenID。[获取OpenID文档](https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-3)
   */
  @SerializedName("openid")
  @Expose(serialize = false)
  private String openid;

  public String getCouponCode() {
    return couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryCouponRequest {\n");
    sb.append("    couponCode: ").append(toIndentedString(couponCode)).append("\n");
    sb.append("    appid: ").append(toIndentedString(appid)).append("\n");
    sb.append("    openid: ").append(toIndentedString(openid)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
