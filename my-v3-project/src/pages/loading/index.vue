<template>
  <view class="container loading-page">
    <LoadingMagic :text="tips[current]" />
    <view class="subtle hint">正在结合城市生活节奏与行为习惯，请勿重复提交。</view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import LoadingMagic from '../../components/LoadingMagic.vue';
import { calculate } from '../../api/wealth';

const current = ref(0);
const tips = [
  '正在读取城市生活节奏...',
  '正在分析你的行为习惯...',
  '正在生成成长类型画像...',
  '正在整理7天轻量行动建议...'
];

onMounted(async () => {
  const timer = setInterval(() => {
    current.value = (current.value + 1) % tips.length;
  }, 900);
  try {
    const payload = uni.getStorageSync('pendingCalculate');
    const report = await calculate(payload);
    uni.setStorageSync('currentReport', report);
    clearInterval(timer);
    uni.redirectTo({ url: '/pages/report/index' });
  } catch (e) {
    clearInterval(timer);
    setTimeout(() => uni.navigateBack(), 800);
  }
});
</script>

<style scoped>
.loading-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.hint {
  margin-top: 36rpx;
  text-align: center;
}
</style>
