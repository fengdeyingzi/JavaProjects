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

/** CouponSendGovCardResponse */
public class CouponSendGovCardResponse {
  /** 消费卡code 说明：消费卡card_id下的code */
  @SerializedName("card_code")
  private String cardCode;

  public String getCardCode() {
    return cardCode;
  }

  public void setCardCode(String cardCode) {
    this.cardCode = cardCode;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CouponSendGovCardResponse {\n");
    sb.append("    cardCode: ").append(toIndentedString(cardCode)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
