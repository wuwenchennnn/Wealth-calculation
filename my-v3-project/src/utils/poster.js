export async function createPoster(report) {
  const text = `${report.city || '你的城市'} · 人生财富宿命报告\n\n${report.summaryReport || ''}\n\n本测算仅为趣味娱乐参考`;
  uni.setClipboardData({ data: text, showToast: false });
  uni.showToast({ title: '海报文案已复制，可接入canvas插件生成高清图', icon: 'none' });
  return text;
}
