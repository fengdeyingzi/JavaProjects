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

/** Coupon */
public class Coupon {
  /** 创建批次的商户号 说明：微信为创建方商户分配的商户号 */
  @SerializedName("stock_creator_mchid")
  private String stockCreatorMchid;

  /** 批次号 说明：批次id */
  @SerializedName("stock_id")
  private String stockId;

  /** 单品优惠特定信息 说明：单品优惠特定信息 */
  @SerializedName("cut_to_message")
  private CutTypeMsg cutToMessage;

  /** 代金券名称 说明：代金券名称 */
  @SerializedName("coupon_name")
  private String couponName;

  /** 代金券状态 说明：代金券状态：SENDED-可用，USED-已实扣，EXPIRED-已过期 */
  @SerializedName("status")
  private String status;

  /** 使用说明 说明：代金券描述说明字段 */
  @SerializedName("description")
  private String description;

  /** 领券时间 说明：领券时间 */
  @SerializedName("create_time")
  private String createTime;

  /** 券类型 说明：NORMAL-满减券；CUT_TO-减至券 */
  @SerializedName("coupon_type")
  private String couponType;

  /** 是否无资金流 说明：true-是；false-否 */
  @SerializedName("no_cash")
  private Boolean noCash;

  /** 可用开始时间 说明：可用开始时间 */
  @SerializedName("available_begin_time")
  private String availableBeginTime;

  /** 可用结束时间 说明：可用结束时间 */
  @SerializedName("available_end_time")
  private String availableEndTime;

  /** 是否单品优惠 说明：TRUE-是；FALSE-否 */
  @SerializedName("singleitem")
  private Boolean singleitem;

  /** 满减券信息 说明：普通满减券面额、门槛信息 */
  @SerializedName("normal_coupon_information")
  private FixedValueStockMsg normalCouponInformation;

  public String getStockCreatorMchid() {
    return stockCreatorMchid;
  }

  public void setStockCreatorMchid(String stockCreatorMchid) {
    this.stockCreatorMchid = stockCreatorMchid;
  }

  public String getStockId() {
    return stockId;
  }

  public void setStockId(String stockId) {
    this.stockId = stockId;
  }

  public CutTypeMsg getCutToMessage() {
    return cutToMessage;
  }

  public void setCutToMessage(CutTypeMsg cutToMessage) {
    this.cutToMessage = cutToMessage;
  }

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getCouponType() {
    return couponType;
  }

  public void setCouponType(String couponType) {
    this.couponType = couponType;
  }

  public Boolean getNoCash() {
    return noCash;
  }

  public void setNoCash(Boolean noCash) {
    this.noCash = noCash;
  }

  public String getAvailableBeginTime() {
    return availableBeginTime;
  }

  public void setAvailableBeginTime(String availableBeginTime) {
    this.availableBeginTime = availableBeginTime;
  }

  public String getAvailableEndTime() {
    return availableEndTime;
  }

  public void setAvailableEndTime(String availableEndTime) {
    this.availableEndTime = availableEndTime;
  }

  public Boolean getSingleitem() {
    return singleitem;
  }

  public void setSingleitem(Boolean singleitem) {
    this.singleitem = singleitem;
  }

  public FixedValueStockMsg getNormalCouponInformation() {
    return normalCouponInformation;
  }

  public void setNormalCouponInformation(FixedValueStockMsg normalCouponInformation) {
    this.normalCouponInformation = normalCouponInformation;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Coupon {\n");
    sb.append("    stockCreatorMchid: ").append(toIndentedString(stockCreatorMchid)).append("\n");
    sb.append("    stockId: ").append(toIndentedString(stockId)).append("\n");
    sb.append("    cutToMessage: ").append(toIndentedString(cutToMessage)).append("\n");
    sb.append("    couponName: ").append(toIndentedString(couponName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    couponType: ").append(toIndentedString(couponType)).append("\n");
    sb.append("    noCash: ").append(toIndentedString(noCash)).append("\n");
    sb.append("    availableBeginTime: ").append(toIndentedString(availableBeginTime)).append("\n");
    sb.append("    availableEndTime: ").append(toIndentedString(availableEndTime)).append("\n");
    sb.append("    singleitem: ").append(toIndentedString(singleitem)).append("\n");
    sb.append("    normalCouponInformation: ")
        .append(toIndentedString(normalCouponInformation))
        .append("\n");
    sb.append("}");
    return sb.toString();
  }
}
