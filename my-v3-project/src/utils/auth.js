export function getVisitorId() {
  let visitorId = uni.getStorageSync('visitorId');
  if (!visitorId) {
    visitorId = `guest_${Date.now()}_${Math.random().toString(16).slice(2)}`;
    uni.setStorageSync('visitorId', visitorId);
  }
  return visitorId;
}

