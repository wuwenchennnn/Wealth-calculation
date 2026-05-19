<template>
  <view class="container loading-page">
    <LoadingMagic :text="tips[current]" />
    <view class="subtle hint">正在结合城市生活成本与AI报告模型，请勿重复提交。</view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import LoadingMagic from '../../components/LoadingMagic.vue';
import { calculate } from '../../api/wealth';

const current = ref(0);
const tips = [
  '正在读取城市生活压力指数...',
  '正在匹配同龄人财富段位...',
  '正在生成你的财富时间轴...',
  '正在压缩30%精简报告...'
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
