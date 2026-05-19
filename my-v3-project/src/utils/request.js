import { API_BASE_URL } from '../config';

export function request(options) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${API_BASE_URL}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'content-type': 'application/json',
        ...(options.header || {})
      },
      success: (res) => {
        const body = res.data || {};
        if (body.code === 0) {
          resolve(body.data);
          return;
        }
        uni.showToast({ title: body.message || '请求失败', icon: 'none' });
        reject(body);
      },
      fail: (err) => {
        uni.showToast({ title: '网络连接失败', icon: 'none' });
        reject(err);
      }
    });
  });
}
