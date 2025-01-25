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
import java.util.List;

/** CouponCodeListResponse */
public class CouponCodeListResponse {
  /** 批次号 说明：商家券批次号 */
  @SerializedName("stock_id")
  private String stockId;

  /** 总数 说明：该批次已上传code总个数 */
  @SerializedName("total_count")
  private Long totalCount;

  /** 查询code结果列表 说明：查询code的结果列表 */
  @SerializedName("data")
  private List<CouponCodeEntity> data;

  /** 分页起始位置 说明：分页起始位置，与请求相同 */
  @SerializedName("offset")
  private Long offset;

  /** 返回数据的个数 说明：返回数据的实际个数 */
  @SerializedName("limit")
  private Long limit;

  public String getStockId() {
    return stockId;
  }

  public void setStockId(String stockId) {
    this.stockId = stockId;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public List<CouponCodeEntity> getData() {
    return data;
  }

  public void setData(List<CouponCodeEntity> data) {
    this.data = data;
  }

  public Long getOffset() {
    return offset;
  }

  public void setOffset(Long offset) {
    this.offset = offset;
  }

  public Long getLimit() {
    return limit;
  }

  public void setLimit(Long limit) {
    this.limit = limit;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CouponCodeListResponse {\n");
    sb.append("    stockId: ").append(toIndentedString(stockId)).append("\n");
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
