<template>
  <view class="container">
    <view class="page-title">填写你的6项财富画像</view>
    <view class="subtle">所有信息仅用于娱乐测算展示，不提供金融建议。</view>

    <view class="card form-card">
      <picker :range="cityOptions" @change="onCityChange">
        <view :class="['input', !form.city && 'placeholder']">{{ form.city || '请选择所在城市' }}</view>
      </picker>
      <view class="city-tip">未列出的城市请选择“其他城市”，将按普通城市生活成本保守估算。</view>
      <input v-model.number="form.age" class="input" type="number" placeholder="年龄" />
      <input v-model.number="form.salary" class="input" type="digit" placeholder="税后月收入" />
      <input v-model.number="form.money" class="input" type="digit" placeholder="当前存款" />
      <picker :range="consumeOptions" @change="onConsumeChange">
        <view :class="['input', !form.consume && 'placeholder']">{{ form.consume || '每月消费档位' }}</view>
      </picker>
      <picker :range="workOptions" @change="onWorkChange">
        <view :class="['input', !form.workType && 'placeholder']">{{ form.workType || '工作状态' }}</view>
      </picker>
    </view>

    <view class="primary-btn" @tap="submit">开始AI测算</view>
  </view>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { getCityList } from '../../api/wealth';
import { getVisitorId } from '../../utils/auth';

const defaultCityOptions = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '南京', '西安', '重庆', '其他城市'];
const cityOptions = ref(defaultCityOptions);
const consumeOptions = ['节制型', '普通型', '享受型', '月光型'];
const workOptions = ['稳定上班', '互联网打工', '自由职业', '创业中', '待业调整'];

const form = reactive({
  city: '',
  age: '',
  salary: '',
  money: '',
  consume: '',
  workType: ''
});

onMounted(async () => {
  try {
    const cities = await getCityList();
    if (Array.isArray(cities) && cities.length) {
      cityOptions.value = cities;
    }
  } catch (error) {
    cityOptions.value = defaultCityOptions;
  }
});

const onCityChange = (e) => {
  form.city = cityOptions.value[e.detail.value];
};

const onConsumeChange = (e) => {
  form.consume = consumeOptions[e.detail.value];
};

const onWorkChange = (e) => {
  form.workType = workOptions[e.detail.value];
};

const submit = () => {
  if (!form.city || !form.age || !form.salary || form.money === '' || !form.consume || !form.workType) {
    uni.showToast({ title: '请完整填写6项信息', icon: 'none' });
    return;
  }
  const payload = { ...form, openid: getVisitorId() };
  uni.setStorageSync('pendingCalculate', payload);
  uni.navigateTo({ url: '/pages/loading/index' });
};
</script>

<style scoped>
.page-title {
  padding-top: 24rpx;
  font-size: 44rpx;
  font-weight: 800;
}
.form-card {
  margin: 34rpx 0;
  padding: 24rpx;
}
.input {
  height: 92rpx;
  line-height: 92rpx;
  padding: 0 24rpx;
  margin-bottom: 20rpx;
  border-radius: 20rpx;
  background: rgba(255, 255, 255, .1);
  color: #fff;
  font-size: 30rpx;
}
.placeholder {
  color: rgba(255, 255, 255, .48);
}
.city-tip {
  margin: -8rpx 4rpx 20rpx;
  color: rgba(255, 255, 255, .56);
  font-size: 24rpx;
  line-height: 1.55;
}
</style>
