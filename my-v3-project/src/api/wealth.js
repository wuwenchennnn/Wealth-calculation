import { request } from '../utils/request';

export const getCityList = () => request({ url: '/city/list' });
export const getCityCost = (city) => request({ url: `/city/cost?city=${encodeURIComponent(city)}` });
export const calculate = (data) => request({ url: '/calculate', method: 'POST', data });
export const unlockReport = (data) => request({ url: '/calculate/unlock', method: 'POST', data });
export const getAdsConfig = () => request({ url: '/ads/config' });
