<template>
  <view class="container">
    <view class="title">你的财富宿命报告</view>
    <view class="notice">本测算仅为趣味娱乐参考，不构成任何金融或理财建议。</view>

    <view class="report-list">
      <view
        v-for="(block, index) in reportBlocks"
        :key="index"
        :class="['report-block', block.type]"
      >
        <text v-if="block.type !== 'divider'">{{ block.text }}</text>
      </view>
    </view>

    <view v-if="!report.unlocked" class="unlock-card card">
      <view class="unlock-title">完整6大模块报告已生成</view>
      <view class="subtle">观看激励视频广告后，永久解锁完整报告与高清分享海报。</view>
      <view class="primary-btn" @tap="unlock">观看广告解锁完整报告</view>
    </view>

    <view v-else class="actions">
      <view class="primary-btn" @tap="poster">生成高清分享海报</view>
    </view>

    <BannerAd :unit-id="adConfig.bannerAdUnitId" />
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import BannerAd from '../../components/BannerAd.vue';
import { unlockReport } from '../../api/wealth';
import { getVisitorId } from '../../utils/auth';
import { showInterstitial, showRewardAd } from '../../utils/ad';
import { AD_CONFIG as adConfig } from '../../config';

const report = ref({});
const visibleReport = computed(() => report.value.unlocked ? report.value.fullReport : report.value.summaryReport);
const reportBlocks = computed(() => parseReport(visibleReport.value));

const parseReport = (text = '') => {
  return text
    .replace(/\r\n/g, '\n')
    .split('\n')
    .map((line) => line.trim())
    .filter(Boolean)
    .map((line) => {
      const cleanText = normalizeMarkdown(line);
      return {
        type: getBlockType(line, cleanText),
        text: cleanText
      };
    });
};

const normalizeMarkdown = (line) => line
  .replace(/^#{1,6}\s*/, '')
  .replace(/^[-*]\s+/, '• ')
  .replace(/^\|/, '')
  .replace(/\|$/,'')
  .replace(/\|/g, '  ')
  .replace(/\*\*/g, '')
  .replace(/`/g, '')
  .trim();

const getBlockType = (rawLine, cleanText) => {
  if (/^---+$/.test(rawLine)) return 'divider';
  if (/^#\s+/.test(rawLine) || cleanText.includes('《') || cleanText.includes('报告')) return 'main-heading';
  if (/^##\s+/.test(rawLine) || /^\d+[.、️⃣]/.test(cleanText) || /【.*】/.test(cleanText)) return 'section-heading';
  if (/^###\s+/.test(rawLine) || cleanText.startsWith('📌') || cleanText.startsWith('🔮')) return 'sub-heading';
  if (/^[-*]\s+/.test(rawLine) || cleanText.startsWith('•')) return 'bullet';
  if (cleanText.startsWith('｜') || cleanText.includes('数据参考') || cleanText.includes('恭喜你')) return 'highlight';
  return 'paragraph';
};

onShow(() => {
  report.value = uni.getStorageSync('currentReport') || {};
  showInterstitial(adConfig.interstitialAdUnitId);
});

const unlock = async () => {
  const watched = await showRewardAd(adConfig.rewardAdUnitId);
  if (!watched) {
    uni.showToast({ title: '需要完整观看广告才可解锁', icon: 'none' });
    return;
  }
  const res = await unlockReport({ recordId: report.value.recordId, openid: getVisitorId(), adReceipt: `reward_${Date.now()}` });
  report.value = res;
  uni.setStorageSync('currentReport', res);
};

const poster = () => {
  uni.setStorageSync('posterReport', report.value);
  uni.navigateTo({ url: '/pages/poster/index' });
};
</script>

<style scoped>
.title {
  font-size: 44rpx;
  font-weight: 900;
}
.notice {
  margin: 20rpx 0 28rpx;
  color: #f7c873;
  font-size: 24rpx;
}
.report-list {
  display: flex;
  flex-direction: column;
  gap: 18rpx;
}
.report-block {
  color: rgba(255, 255, 255, .9);
  font-size: 29rpx;
  line-height: 1.78;
  word-break: break-word;
}
.main-heading {
  padding: 34rpx 30rpx;
  border-radius: 30rpx;
  background: linear-gradient(135deg, rgba(247, 200, 115, .24), rgba(255, 255, 255, .08));
  color: #fff4cf;
  font-size: 38rpx;
  font-weight: 900;
  line-height: 1.45;
  box-shadow: 0 16rpx 42rpx rgba(0, 0, 0, .16);
}
.section-heading {
  margin-top: 18rpx;
  padding: 26rpx 28rpx;
  border-radius: 26rpx;
  background: rgba(247, 200, 115, .16);
  border: 1rpx solid rgba(247, 200, 115, .24);
  color: #f7c873;
  font-size: 34rpx;
  font-weight: 900;
  line-height: 1.45;
}
.sub-heading {
  margin-top: 10rpx;
  padding: 20rpx 24rpx;
  border-left: 8rpx solid #f7c873;
  border-radius: 18rpx;
  background: rgba(255, 255, 255, .08);
  color: #fff;
  font-size: 31rpx;
  font-weight: 800;
}
.paragraph {
  padding: 0 6rpx;
  color: rgba(255, 255, 255, .86);
}
.bullet {
  padding: 18rpx 22rpx;
  border-radius: 18rpx;
  background: rgba(255, 255, 255, .07);
  color: rgba(255, 255, 255, .88);
  font-size: 28rpx;
  line-height: 1.65;
}
.highlight {
  padding: 22rpx 24rpx;
  border-radius: 22rpx;
  background: rgba(92, 225, 163, .12);
  border: 1rpx solid rgba(92, 225, 163, .2);
  color: #c9ffe7;
  font-size: 29rpx;
  font-weight: 700;
}
.divider {
  height: 1rpx;
  margin: 8rpx 0;
  background: rgba(255, 255, 255, .12);
}
.unlock-card {
  margin-top: 32rpx;
  padding: 32rpx;
}
.unlock-title {
  margin-bottom: 16rpx;
  font-size: 34rpx;
  font-weight: 800;
}
.unlock-card .primary-btn,
.actions .primary-btn {
  margin-top: 28rpx;
}
</style>
