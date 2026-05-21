const env = import.meta.env || {};

console.log('VITE_API_BASE_URL=', env.VITE_API_BASE_URL);

export const API_BASE_URL = env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const AD_CONFIG = {
  rewardAdUnitId: env.VITE_REWARD_AD_UNIT_ID || 'adunit-demo-reward',
  interstitialAdUnitId: env.VITE_INTERSTITIAL_AD_UNIT_ID || 'adunit-demo-interstitial',
  bannerAdUnitId: env.VITE_BANNER_AD_UNIT_ID || 'adunit-demo-banner'
};
