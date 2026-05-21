<template>
  <view class="container poster-page">
    <view class="title">分享海报</view>
    <view class="notice">生成长图后，可保存完整趣味画像到相册。</view>

    <view class="preview-card card" :style="{ minHeight: previewHeight + 'rpx' }">
      <canvas
        canvas-id="posterCanvas"
        id="posterCanvas"
        class="poster-canvas"
        :style="{ width: canvasWidth + 'px', height: canvasHeight + 'px' }"
      />
      <image v-if="posterPath" class="poster-image" :src="posterPath" mode="widthFix" />
    </view>

    <view class="actions">
      <view class="primary-btn" @tap="savePoster">保存完整长图</view>
      <view class="secondary-btn" @tap="redrawPoster">重新生成</view>
    </view>
  </view>
</template>

<script setup>
import { computed, nextTick, ref } from 'vue';
import { onReady } from '@dcloudio/uni-app';

const report = ref({});
const posterPath = ref('');
const canvasWidth = 300;
const canvasHeight = ref(900);
const exportScale = 2;
const previewHeight = computed(() => Math.max(960, canvasHeight.value * 2));

onReady(async () => {
  report.value = uni.getStorageSync('posterReport') || uni.getStorageSync('currentReport') || {};
  await nextTick();
  drawPoster();
});

const redrawPoster = () => {
  posterPath.value = '';
  drawPoster();
};

const drawPoster = async () => {
  const sections = buildPosterSections();
  canvasHeight.value = calcPosterHeight(sections);
  await nextTick();

  const ctx = uni.createCanvasContext('posterCanvas');
  const w = canvasWidth;
  const h = canvasHeight.value;
  const padding = 24;

  drawBackground(ctx, w, h);
  drawHeader(ctx, padding);

  let y = 112;
  y = drawUserCard(ctx, padding, y, w - padding * 2);
  y += 14;
  sections.forEach((section) => {
    y = drawSectionCard(ctx, section, padding, y, w - padding * 2);
    y += 12;
  });
  drawFooter(ctx, padding, h);

  ctx.draw(false, () => {
    setTimeout(() => exportPoster(), 300);
  });
};

const calcPosterHeight = (sections) => {
  const contentWidth = canvasWidth - 72;
  let height = 112 + 74 + 14 + 72;
  sections.forEach((section) => {
    height += 46;
    section.lines.forEach((line) => {
      height += estimateLineCount(line, contentWidth, 12) * 18 + 4;
    });
    height += 28;
  });
  return Math.min(Math.max(height, 900), 3600);
};

const estimateLineCount = (text, maxWidth, fontSize) => {
  const approxCharsPerLine = Math.max(10, Math.floor(maxWidth / (fontSize * 0.9)));
  return Math.max(1, Math.ceil(text.length / approxCharsPerLine));
};

const drawBackground = (ctx, w, h) => {
  const bg = ctx.createLinearGradient(0, 0, 0, h);
  bg.addColorStop(0, '#191b31');
  bg.addColorStop(1, '#0d0f1a');
  ctx.setFillStyle(bg);
  ctx.fillRect(0, 0, w, h);

  ctx.setFillStyle('rgba(111, 76, 255, 0.32)');
  ctx.arc(64, 28, 92, 0, Math.PI * 2);
  ctx.fill();

  ctx.setFillStyle('rgba(247, 200, 115, 0.15)');
  ctx.arc(w - 28, 112, 78, 0, Math.PI * 2);
  ctx.fill();
};

const drawHeader = (ctx, padding) => {
  ctx.setFillStyle('#f7c873');
  ctx.setFontSize(15);
  ctx.fillText('休闲娱乐 · 趣味测评', padding, 34);

  ctx.setFillStyle('#ffffff');
  ctx.setFontSize(25);
  ctx.fillText('AI人生画像报告', padding, 68);

  ctx.setFillStyle('rgba(255, 255, 255, 0.62)');
  ctx.setFontSize(12);
  ctx.fillText('AI生成 · 自我观察 · 娱乐参考', padding, 91);
};

const drawUserCard = (ctx, x, y, w) => {
  drawRoundRect(ctx, x, y, w, 74, 14, 'rgba(255, 255, 255, 0.09)');

  ctx.setFillStyle('#f7c873');
  ctx.setFontSize(18);
  ctx.fillText(`${report.value.city || '你的城市'} · 财富画像`, x + 16, y + 30);

  ctx.setFillStyle('rgba(255, 255, 255, 0.82)');
  ctx.setFontSize(13);
  const meta = extractMeta();
  drawWrapText(ctx, meta, x + 16, y + 55, w - 32, 18, 1);
  return y + 74;
};

const drawSectionCard = (ctx, section, x, y, w) => {
  const innerWidth = w - 32;
  const contentHeight = section.lines.reduce((sum, line) => sum + estimateLineCount(line, innerWidth, 12) * 18 + 4, 0);
  const cardHeight = Math.max(82, 42 + contentHeight + 18);

  drawRoundRect(ctx, x, y, w, cardHeight, 16, 'rgba(255, 255, 255, 0.10)');

  ctx.setFillStyle('#f7c873');
  ctx.setFontSize(16);
  drawWrapText(ctx, section.title, x + 16, y + 28, innerWidth, 18, 2);

  let textY = y + 52;
  section.lines.forEach((line) => {
    const isBullet = line.startsWith('•');
    ctx.setFillStyle(isBullet ? '#fff4cf' : 'rgba(255, 255, 255, 0.84)');
    ctx.setFontSize(12);
    textY = drawWrapText(ctx, line, x + 16, textY, innerWidth, 18, 12) + 4;
  });

  return y + cardHeight;
};

const drawFooter = (ctx, padding, h) => {
  ctx.setFillStyle('rgba(247, 200, 115, 0.14)');
  ctx.fillRect(padding, h - 66, canvasWidth - padding * 2, 1);

  ctx.setFillStyle('rgba(255, 255, 255, 0.72)');
  ctx.setFontSize(12);
  ctx.fillText('本报告仅供休闲娱乐与自我观察', padding, h - 42);

  ctx.setFillStyle('rgba(255, 255, 255, 0.44)');
  ctx.setFontSize(10);
  ctx.fillText('长按保存图片，邀请朋友一起测一测', padding, h - 22);
};

const buildPosterSections = () => {
  const text = report.value.fullReport || report.value.summaryReport || '';
  const lines = text.split('\n').map((line) => cleanLine(line)).filter(Boolean);
  const sections = [];
  let current = null;

  lines.forEach((line) => {
    if (shouldSkipLine(line)) {
      return;
    }
    if (isSectionTitle(line)) {
      if (current) sections.push(current);
      current = { title: normalizeSectionTitle(line), lines: [] };
      return;
    }
    if (current) {
      current.lines.push(line);
    }
  });

  if (current) sections.push(current);
  return sections.slice(0, 6).map(toSummarySection);
};

const shouldSkipLine = (line) => {
  return line.startsWith('📍')
    || line.includes('你的《AI人生画像报告》')
    || line.includes('完整6大模块报告待解锁后查看')
    || line.includes('本报告仅为休闲娱乐');
};

const isSectionTitle = (line) => /^\d+[.、]/.test(line) || /【.*】/.test(line);

const normalizeSectionTitle = (title) => {
  const text = title.replace(/^#+\s*/, '').replace(/——\s*/g, '——').trim();
  return text.length > 24 ? `${text.slice(0, 24)}…` : text;
};

const toSummarySection = (section) => {
  const bulletLines = section.lines
    .filter((line) => line.startsWith('•'))
    .map((line) => compactText(line, 34))
    .slice(0, 2);
  const paragraphText = section.lines
    .filter((line) => !line.startsWith('•'))
    .join(' ');
  const summary = compactText(pickUsefulSentence(paragraphText), 64);
  const lines = [summary, ...bulletLines].filter(Boolean);
  return {
    title: section.title,
    lines: lines.length ? lines : ['这一部分建议已生成，打开完整报告可查看详情。']
  };
};

const pickUsefulSentence = (text) => {
  const sentences = text
    .replace(/\s+/g, ' ')
    .split(/[。！？!?.]/)
    .map((item) => item.trim())
    .filter(Boolean);
  if (!sentences.length) {
    return text;
  }
  const best = sentences.find((item) => item.length >= 18) || sentences[0];
  return best;
};

const compactText = (text = '', maxLength = 60) => {
  const normalized = text.replace(/\s+/g, ' ').trim();
  if (normalized.length <= maxLength) {
    return normalized;
  }
  return `${normalized.slice(0, maxLength)}…`;
};

const extractMeta = () => {
  const text = report.value.fullReport || report.value.summaryReport || '';
  const metaLine = text.split('\n').map((line) => cleanLine(line)).find((line) => line.startsWith('📍'));
  return metaLine || `${report.value.city || '你的城市'} · AI人生画像报告`;
};

const cleanLine = (line = '') => line
  .replace(/^#{1,6}\s*/, '')
  .replace(/^[-*]\s+/, '• ')
  .replace(/\*\*/g, '')
  .replace(/`/g, '')
  .replace(/^---+$/, '')
  .trim();

const drawRoundRect = (ctx, x, y, w, h, r, color) => {
  ctx.beginPath();
  ctx.moveTo(x + r, y);
  ctx.arcTo(x + w, y, x + w, y + h, r);
  ctx.arcTo(x + w, y + h, x, y + h, r);
  ctx.arcTo(x, y + h, x, y, r);
  ctx.arcTo(x, y, x + w, y, r);
  ctx.closePath();
  ctx.setFillStyle(color);
  ctx.fill();
};

const drawWrapText = (ctx, text, x, y, maxWidth, lineHeight, maxLines = 99) => {
  let line = '';
  let lineCount = 0;
  for (let i = 0; i < text.length; i += 1) {
    const char = text[i];
    const testLine = line + char;
    if (ctx.measureText(testLine).width > maxWidth && line) {
      lineCount += 1;
      if (lineCount >= maxLines) {
        ctx.fillText(`${line.slice(0, Math.max(0, line.length - 1))}…`, x, y);
        return y + lineHeight;
      }
      ctx.fillText(line, x, y);
      line = char;
      y += lineHeight;
    } else {
      line = testLine;
    }
  }
  if (line && lineCount < maxLines) {
    ctx.fillText(line, x, y);
  }
  return y + lineHeight;
};

const exportPoster = () => {
  uni.canvasToTempFilePath({
    canvasId: 'posterCanvas',
    width: canvasWidth,
    height: canvasHeight.value,
    destWidth: canvasWidth * exportScale,
    destHeight: canvasHeight.value * exportScale,
    success: (res) => {
      posterPath.value = res.tempFilePath;
    },
    fail: () => {
      uni.showToast({ title: '海报生成失败，请重试', icon: 'none' });
    }
  });
};

const savePoster = () => {
  if (!posterPath.value) {
    uni.showToast({ title: '海报还在生成中', icon: 'none' });
    return;
  }
  uni.saveImageToPhotosAlbum({
    filePath: posterPath.value,
    success: () => uni.showToast({ title: '已保存到相册' }),
    fail: () => uni.showToast({ title: '保存失败，请检查相册权限', icon: 'none' })
  });
};
</script>

<style scoped>
.poster-page {
  padding-bottom: 80rpx;
}
.title {
  font-size: 44rpx;
  font-weight: 900;
}
.notice {
  margin: 20rpx 0 28rpx;
  color: #f7c873;
  font-size: 24rpx;
}
.preview-card {
  position: relative;
  width: 600rpx;
  margin: 0 auto 34rpx;
  padding: 0;
  overflow: hidden;
}
.poster-canvas {
  position: absolute;
  left: -9999px;
  top: -9999px;
  display: block;
}
.poster-image {
  width: 600rpx;
  display: block;
  border-radius: 32rpx;
}
.actions {
  display: flex;
  flex-direction: column;
  gap: 22rpx;
}
.secondary-btn {
  height: 88rpx;
  border-radius: 999rpx;
  border: 1rpx solid rgba(247, 200, 115, .42);
  color: #f7c873;
  font-size: 30rpx;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, .06);
}
</style>
