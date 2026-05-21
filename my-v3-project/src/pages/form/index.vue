<template>
  <view class="container">
    <view class="page-title">填写你的成长画像信息</view>
    <view class="subtle">本测评仅用于休闲娱乐和自我观察，不构成职业、心理、金融、投资或医疗建议；请勿填写身份证、手机号、银行卡等敏感信息。</view>

    <view class="card form-card">
      <picker :range="cityOptions" @change="onPickerChange('city', cityOptions, $event)">
        <view :class="['input', !form.city && 'placeholder']">{{ form.city || '请选择所在城市' }}</view>
      </picker>
      <view class="city-tip">城市仅用于参考生活节奏、通勤压力和机会密度，不做排名或专业判断。</view>

      <input v-model.number="form.age" class="input" type="number" placeholder="年龄" />

      <picker :range="statusOptions" @change="onPickerChange('currentStatus', statusOptions, $event)">
        <view :class="['input', !form.currentStatus && 'placeholder']">{{ form.currentStatus || '当前状态' }}</view>
      </picker>

      <picker :range="disposableOptions" @change="onPickerChange('disposableRange', disposableOptions, $event)">
        <view :class="['input', !form.disposableRange && 'placeholder']">{{ form.disposableRange || '每月可支配区间' }}</view>
      </picker>

      <picker :range="reserveOptions" @change="onPickerChange('existingSavings', reserveOptions, $event)">
        <view :class="['input', !form.existingSavings && 'placeholder']">{{ form.existingSavings || '现有积蓄' }}</view>
      </picker>

      <picker :range="rhythmOptions" @change="onPickerChange('lifeRhythm', rhythmOptions, $event)">
        <view :class="['input', !form.lifeRhythm && 'placeholder']">{{ form.lifeRhythm || '生活节奏' }}</view>
      </picker>

      <picker :range="habitOptions" @change="onPickerChange('spendingHabit', habitOptions, $event)">
        <view :class="['input', !form.spendingHabit && 'placeholder']">{{ form.spendingHabit || '消费习惯' }}</view>
      </picker>

      <picker :range="actionOptions" @change="onPickerChange('actionStyle', actionOptions, $event)">
        <view :class="['input', !form.actionStyle && 'placeholder']">{{ form.actionStyle || '行动力自评' }}</view>
      </picker>
    </view>

    <view class="primary-btn" @tap="submit">生成我的人生画像</view>
  </view>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { getCityList } from '../../api/wealth';
import { getVisitorId } from '../../utils/auth';

const defaultCityOptions = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '南京', '西安', '重庆', '其他城市'];
const cityOptions = ref(defaultCityOptions);
const statusOptions = ['稳定上班', '互联网/高强度工作', '自由职业', '创业探索', '学生/备考', '待业调整', '其他'];
const disposableOptions = ['3000以下', '3000-6000', '6000-10000', '10000-20000', '20000以上', '暂不填写'];
const reserveOptions = ['几乎没有', '1万元以内', '1万-5万', '5万-20万', '20万以上', '暂不填写'];
const rhythmOptions = ['比较规律', '忙但还能掌控', '经常熬夜赶进度', '作息不稳定', '最近比较躺平'];
const habitOptions = ['节制规划型', '日常均衡型', '体验优先型', '情绪消费型', '月光压力型'];
const actionOptions = ['想到就做', '计划很多但执行一般', '需要外部推动', '容易三分钟热度', '最近状态低迷'];

const form = reactive({
  city: '',
  age: '',
  currentStatus: '',
  disposableRange: '',
  existingSavings: '',
  lifeRhythm: '',
  spendingHabit: '',
  actionStyle: ''
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

const onPickerChange = (field, options, e) => {
  form[field] = options[e.detail.value];
};

const submit = () => {
  const requiredFields = ['city', 'age', 'currentStatus', 'disposableRange', 'existingSavings', 'lifeRhythm', 'spendingHabit', 'actionStyle'];
  if (requiredFields.some((field) => form[field] === '' || form[field] === null || form[field] === undefined)) {
    uni.showToast({ title: '请完整填写8项信息', icon: 'none' });
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
