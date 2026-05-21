let defaultApiBaseUrl = 'http://localhost:8080/api';
// #ifdef MP-WEIXIN
defaultApiBaseUrl = 'http://10.103.35.28:8080/api';
// #endif

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || defaultApiBaseUrl;

console.log('VITE_API_BASE_URL=', import.meta.env.VITE_API_BASE_URL, 'API_BASE_URL=', API_BASE_URL);

export const AD_CONFIG = {
  rewardAdUnitId: import.meta.env.VITE_REWARD_AD_UNIT_ID || 'adunit-demo-reward',
  interstitialAdUnitId: import.meta.env.VITE_INTERSTITIAL_AD_UNIT_ID || 'adunit-demo-interstitial',
  bannerAdUnitId: import.meta.env.VITE_BANNER_AD_UNIT_ID || 'adunit-demo-banner'
};
