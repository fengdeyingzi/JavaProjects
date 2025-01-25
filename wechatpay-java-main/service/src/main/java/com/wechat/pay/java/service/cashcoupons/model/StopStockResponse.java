// Copyright 2021 Tencent Inc. All rights reserved.
//
// 微信支付营销系统开放API
//
// 新增立减金api
//
// API version: 3.4.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.cashcoupons.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/** StopStockResponse */
public class StopStockResponse {
  /** 暂停时间 说明：暂停时间 */
  @SerializedName("stop_time")
  private String stopTime;

  /** 批次号 说明：批次号 */
  @SerializedName("stock_id")
  private String stockId;

  public String getStopTime() {
    return stopTime;
  }

  public void setStopTime(String stopTime) {
    this.stopTime = stopTime;
  }

  public String getStockId() {
    return stockId;
  }

  public void setStockId(String stockId) {
    this.stockId = stockId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StopStockResponse {\n");
    sb.append("    stopTime: ").append(toIndentedString(stopTime)).append("\n");
    sb.append("    stockId: ").append(toIndentedString(stockId)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
