<template>
  <view class="container home">
    <view class="hero">
      <view class="tag">休闲娱乐 · 趣味测评</view>
      <view class="title">人生潜力研究所</view>
      <view class="desc">AI 基于城市环境、生活节奏与行为习惯，生成你的《AI人生画像报告》。</view>
    </view>

    <view class="card panel">
      <view class="panel-title">你将获得</view>
      <view class="item">成长类型画像</view>
      <view class="item">当前阶段状态</view>
      <view class="item">城市生活节奏参考</view>
      <view class="item">潜力优势分析</view>
      <view class="item">习惯短板提醒</view>
      <view class="item">7天轻量行动建议</view>
    </view>

    <view class="primary-btn" @tap="start">开始趣味测评</view>
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
