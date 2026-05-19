<template>
  <view class="loading-card card">
    <view class="orb"></view>
    <view class="text">{{ text }}</view>
  </view>
</template>

<script setup>
defineProps({
  text: {
    type: String,
    default: 'AI正在读取你的城市财富压力指数...'
  }
});
</script>

<style scoped>
.loading-card {
  padding: 60rpx 32rpx;
  text-align: center;
}
.orb {
  width: 160rpx;
  height: 160rpx;
  margin: 0 auto 32rpx;
  border-radius: 50%;
  background: radial-gradient(circle, #f7c873, #6f4cff 60%, transparent 70%);
  animation: pulse 1.6s infinite alternate;
}
.text {
  font-size: 30rpx;
  color: rgba(255, 255, 255, .86);
}
@keyframes pulse {
  from { transform: scale(.9); opacity: .65; }
  to { transform: scale(1.08); opacity: 1; }
}
</style>
