<script setup>
import { onLaunch } from '@dcloudio/uni-app';

onLaunch(() => {
  uni.setStorageSync('appName', '人生财富宿命测算');
});
</script>

<style>
page {
  min-height: 100%;
  background: #111321;
  color: #fff;
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Arial, sans-serif;
}
</style>
