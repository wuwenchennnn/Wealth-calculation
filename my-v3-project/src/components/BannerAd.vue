<template>
  <view class="ad-bottom" v-if="unitId">
    <ad :unit-id="unitId" ad-type="grid" grid-opacity="0.8" />
  </view>
</template>

<script setup>
defineProps({
  unitId: {
    type: String,
    default: ''
  }
});
</script>
