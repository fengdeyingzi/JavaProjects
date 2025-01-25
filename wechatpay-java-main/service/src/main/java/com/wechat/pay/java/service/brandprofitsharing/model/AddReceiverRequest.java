// Copyright 2021 Tencent Inc. All rights reserved.
//
// 连锁加盟供应链分账API
//
// No description provided (generated by Openapi Generator
// https://github.com/openapitools/openapi-generator)
//
// API version: 1.0.12

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.brandprofitsharing.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** AddReceiverRequest */
public class AddReceiverRequest {
  /** 品牌主商户号 说明：品牌主商户号即绑定具体品牌的商户号，填写微信支付分配的商户号 */
  @SerializedName("brand_mchid")
  private String brandMchid;

  /** 公众账号ID 说明：微信分配的公众账号ID */
  @SerializedName("appid")
  private String appid;

  /** 子商户公众账号ID 说明：品牌主的公众账号ID，分账接收方类型包含PERSONAL_SUB_OPENID时必填 */
  @SerializedName("sub_appid")
  private String subAppid;

  /**
   * 接收方类型 说明：分账接收方类型，枚举值： 1. MERCHANT_ID：商户号（mch_id或者sub_mch_id） 2.
   * PERSONAL_OPENID：个人OpenID（由服务商的AppID转换得到） 3. PERSONAL_SUB_OPENID：个人OpenID（由品牌主的AppID转换得到）
   */
  @SerializedName("type")
  private String type;

  /**
   * 接收方账号 说明：分账接收方账号 1. 分账接收方类型为MERCHANT_ID时，分账接收方账号为商户号（mch_id或者sub_mch_id） 2.
   * 分账接收方类型为PERSONAL_OPENID时，分账接收方账号为个人OpenID（由服务商的AppID转换得到） 3.
   * 分账接收方类型为PERSONAL_SUB_OPENID时，分账接收方账号为个人OpenID（由品牌主的AppID转换得到）
   */
  @SerializedName("account")
  private String account;

  /**
   * 接收方名称 说明：分账接收方的名称。 1. 当type为MERCHANT_ID时，接收方名称必传，且为商户全称。 2.
   * 当type为PERSONAL_OPENID时，接收方名称选传，且为个人姓名。（传入该字段时需要进行校验。） 3.
   * 当type为PERSONAL_SUB_OPENID时，接收方名称选传，且为个人姓名。（传入该字段时需要进行校验。）
   */
  @SerializedName("name")
  private String name;

  /**
   * 与品牌主的关系类型 说明：枚举值： - SUPPLIER：供应商 - DISTRIBUTOR：分销商 - SERVICE_PROVIDER：服务商 - PLATFORM：平台 -
   * STAFF：员工 - OTHERS：其他
   */
  @SerializedName("relation_type")
  private String relationType;

  public String getBrandMchid() {
    return brandMchid;
  }

  public void setBrandMchid(String brandMchid) {
    this.brandMchid = brandMchid;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getSubAppid() {
    return subAppid;
  }

  public void setSubAppid(String subAppid) {
    this.subAppid = subAppid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRelationType() {
    return relationType;
  }

  public void setRelationType(String relationType) {
    this.relationType = relationType;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddReceiverRequest {\n");
    sb.append("    brandMchid: ").append(toIndentedString(brandMchid)).append("\n");
    sb.append("    appid: ").append(toIndentedString(appid)).append("\n");
    sb.append("    subAppid: ").append(toIndentedString(subAppid)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    account: ").append(toIndentedString(account)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    relationType: ").append(toIndentedString(relationType)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
