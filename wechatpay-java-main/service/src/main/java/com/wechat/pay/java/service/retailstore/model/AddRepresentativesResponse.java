// Copyright 2021 Tencent Inc. All rights reserved.
//
// 营销加价购对外API
//
// 指定服务商可通过该接口报名加价购活动、查询某个区域内的加价购活动列表、锁定加价活动购资格以及解锁加价购活动资格。
//
// API version: 1.4.0

// Code generated by WechatPay APIv3 Generator based on [OpenAPI
// Generator](https://openapi-generator.tech); DO NOT EDIT.

package com.wechat.pay.java.service.retailstore.model;

import static com.wechat.pay.java.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** AddRepresentativesResponse */
public class AddRepresentativesResponse {
  /** 零售小店活动ID 说明：零售小店活动ID */
  @SerializedName("activity_id")
  private String activityId;

  /** 添加失败业务代理信息列表 说明：添加失败业务代理信息列表 */
  @SerializedName("failed_representative_info_list")
  private List<RepresentativeInfo> failedRepresentativeInfoList;

  /** 添加时间 说明：添加时间 */
  @SerializedName("add_time")
  private String addTime;

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public List<RepresentativeInfo> getFailedRepresentativeInfoList() {
    return failedRepresentativeInfoList;
  }

  public void setFailedRepresentativeInfoList(
      List<RepresentativeInfo> failedRepresentativeInfoList) {
    this.failedRepresentativeInfoList = failedRepresentativeInfoList;
  }

  public String getAddTime() {
    return addTime;
  }

  public void setAddTime(String addTime) {
    this.addTime = addTime;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddRepresentativesResponse {\n");
    sb.append("    activityId: ").append(toIndentedString(activityId)).append("\n");
    sb.append("    failedRepresentativeInfoList: ")
        .append(toIndentedString(failedRepresentativeInfoList))
        .append("\n");
    sb.append("    addTime: ").append(toIndentedString(addTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
