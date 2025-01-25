// Copyright 2021 Tencent Inc. All rights reserved.
//
// 微信支付分账API
//
// 微信支付分账API
//
// API version: 0.0.9

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.profitsharing.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** QueryMerchantRatioResponse */
public class QueryMerchantRatioResponse {
  /** 子商户号 说明：参考请求参数 */
  @SerializedName("sub_mchid")
  private String subMchid;

  /** 最大分账比例 说明：子商户允许父商户分账的最大比例，单位万分比，比如2000表示20% */
  @SerializedName("max_ratio")
  private Long maxRatio;

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  public Long getMaxRatio() {
    return maxRatio;
  }

  public void setMaxRatio(Long maxRatio) {
    this.maxRatio = maxRatio;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryMerchantRatioResponse {\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("    maxRatio: ").append(toIndentedString(maxRatio)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
