function getWx() {
  return typeof wx === 'undefined' ? null : wx;
}

export function showRewardAd(adUnitId) {
  return new Promise((resolve) => {
    const wxApi = getWx();
    if (!wxApi || !wxApi.createRewardedVideoAd || adUnitId.includes('demo')) {
      setTimeout(() => resolve(true), 600);
      return;
    }
    const ad = wxApi.createRewardedVideoAd({ adUnitId });
    ad.onClose((res) => resolve(Boolean(res && res.isEnded)));
    ad.onError(() => resolve(false));
    ad.load().then(() => ad.show()).catch(() => resolve(false));
  });
}

export function showInterstitial(adUnitId) {
  const wxApi = getWx();
  if (!wxApi || !wxApi.createInterstitialAd || adUnitId.includes('demo')) {
    return;
  }
  const today = new Date().toDateString();
  const key = `interstitial_${today}`;
  const count = Number(uni.getStorageSync(key) || 0);
  if (count >= 3) {
    return;
  }
  const ad = wxApi.createInterstitialAd({ adUnitId });
  ad.load().then(() => {
    uni.setStorageSync(key, count + 1);
    ad.show();
  }).catch(() => {});
}
