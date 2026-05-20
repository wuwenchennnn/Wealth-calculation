<template>
  <view class="container home">
    <view class="hero">
      <view class="tag">人生财富报告 - 智能推算</view>
      <view class="title">AI财富画像</view>
      <view class="desc">无需登录，输入6项极简信息，AI结合城市生活成本，生成你的专属财富报告。</view>
    </view>

    <view class="card panel">
      <view class="panel-title">你将获得</view>
      <view class="item">财富命格判定</view>
      <view class="item">同龄人段位评级</view>
      <view class="item">人生关键财富时间轴</view>
      <view class="item">AI专属逆袭方案</view>
    </view>

    <view class="primary-btn" @tap="start">一键开始测算</view>
    <BannerAd :unit-id="adConfig.bannerAdUnitId" />
  </view>
</template>

<script setup>
import BannerAd from '../../components/BannerAd.vue';
import { AD_CONFIG as adConfig } from '../../config';

const start = () => uni.navigateTo({ url: '/pages/form/index' });
</script>

<style scoped>
.hero {
  padding-top: 80rpx;
}
.tag {
  display: inline-flex;
  padding: 12rpx 22rpx;
  border-radius: 999rpx;
  background: rgba(247, 200, 115, .16);
  color: #f7c873;
  font-size: 24rpx;
}
.title {
  margin-top: 34rpx;
  font-size: 64rpx;
  line-height: 1.15;
  font-weight: 900;
}
.desc {
  margin-top: 24rpx;
  color: rgba(255, 255, 255, .7);
  font-size: 30rpx;
  line-height: 1.7;
}
.panel {
  margin: 56rpx 0 40rpx;
  padding: 34rpx;
}
.panel-title {
  color: #f7c873;
  font-size: 30rpx;
  font-weight: 700;
  margin-bottom: 20rpx;
}
.item {
  padding: 18rpx 0;
  border-bottom: 1rpx solid rgba(255, 255, 255, .08);
  color: rgba(255, 255, 255, .88);
}
</style>
