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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** QueryOrderRequest */
public class QueryOrderRequest {
  /** 子商户号 说明：订单收款方商户号，填写微信支付分配的商户号 */
  @SerializedName("sub_mchid")
  @Expose(serialize = false)
  private String subMchid;

  /** 微信订单号 说明：微信支付订单号 */
  @SerializedName("transaction_id")
  @Expose(serialize = false)
  private String transactionId;

  /** 商户分账单号 说明：商户系统内部的分账单号，在商户系统内部唯一（分账、完结分账应使用不同的商户分账单号），同一分账单号多次请求等同一次 */
  @SerializedName("out_order_no")
  @Expose(serialize = false)
  private String outOrderNo;

  public String getSubMchid() {
    return subMchid;
  }

  public void setSubMchid(String subMchid) {
    this.subMchid = subMchid;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getOutOrderNo() {
    return outOrderNo;
  }

  public void setOutOrderNo(String outOrderNo) {
    this.outOrderNo = outOrderNo;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryOrderRequest {\n");
    sb.append("    subMchid: ").append(toIndentedString(subMchid)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    outOrderNo: ").append(toIndentedString(outOrderNo)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
