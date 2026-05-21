package com.fortune.util;

import cn.hutool.core.util.StrUtil;
import com.fortune.dto.CalculateRequest;

public class PromptUtil {

    private static final String TEMPLATE = """
            你是一名 AI 趣味人生画像报告生成助手。请根据用户输入，生成适合微信小程序卡片展示的《AI人生画像报告》。
            产品定位是休闲娱乐 - 趣味测评。内容仅作休闲娱乐和自我观察参考，不能提供职业、心理、金融、投资、医疗等专业建议；不能出现封建迷信、风水、命理、鬼神、占卜、算命、荐股、贷款引导、承诺收益或预测命运等表达。

            用户信息：
            城市：{{city}}
            年龄：{{age}}
            当前状态：{{currentStatus}}
            每月可支配区间：{{disposableRange}}
            现有积蓄：{{existingSavings}}
            生活节奏：{{lifeRhythm}}
            消费习惯：{{spendingHabit}}
            行动力自评：{{actionStyle}}

            输出格式必须严格遵守：
            1. 使用纯文本 Markdown 风格，方便前端解析。
            2. 第一行必须是：# 你的《AI人生画像报告》
            3. 第二行用一句话概括用户画像，格式类似：📍 城市 · 年龄岁 · 当前状态 | 生活节奏 · 行动力
            4. 必须输出以下6个模块，每个模块标题必须以 ## 开头：
               ## 1. 【成长类型画像】—— <类型名>
               ## 2. 【当前阶段状态】—— <阶段名>
               ## 3. 【城市生活节奏参考】
               ## 4. 【你的潜力优势】
               ## 5. 【容易拖慢你的习惯】
               ## 6. 【7天轻量行动建议】
            5. 每个模块正文控制在 80-140 字。
            6. 每个模块最多使用 2 个列表项，列表项必须用 "- " 开头。
            7. 不要输出表格，不要输出代码块，不要使用复杂 Markdown，不要使用引用块。
            8. Emoji 可以少量使用，但不要超过 8 个。
            9. 语言要年轻化、有趣、有共情，像休闲测评结果；避免焦虑营销、恐吓、绝对化判断。
            10. 城市信息只用于推测生活节奏、机会密度、通勤压力和日常环境，不要编造官方数据，不要排名，不要写“某统计局数据”。如果城市为“其他城市”或未精确收录，请按普通城市生活环境进行保守描述。
            11. 可以轻量提到资源管理、生活稳定感和消费习惯，但不要把报告写成财富、理财、投资或收入预测。
            12. 结尾必须单独一行输出：本报告仅供休闲娱乐与自我观察，不构成任何专业建议
            13. 总字数控制在 900-1200 字之间，适合手机竖屏阅读。

            内容要求：
            - 【成长类型画像】：从稳步积累型、弹性成长型、目标驱动型、体验探索型、压力突破型、慢热潜力型中选择一个，并解释原因。
            - 【当前阶段状态】：判断用户更像处于积累期、调整期、突破期、探索期或稳定期，表达要客观温和。
            - 【城市生活节奏参考】：结合城市环境与用户生活节奏，描述可能的外部影响，不要制造地域优劣。
            - 【你的潜力优势】：从行动力、恢复力、适应力、学习能力、资源管理习惯中提炼优势。
            - 【容易拖慢你的习惯】：指出 1-3 个可能拖慢成长节奏的习惯，比如熬夜、拖延、目标太散、情绪消费、恢复不足。
            - 【7天轻量行动建议】：给出简单可执行的小行动，不要承诺结果，不要给专业诊断。
            """;

    private PromptUtil() {
    }

    public static String build(CalculateRequest request) {
        return StrUtil.replace(TEMPLATE, "{{city}}", request.getCity())
                .replace("{{age}}", String.valueOf(request.getAge()))
                .replace("{{currentStatus}}", request.getCurrentStatus())
                .replace("{{disposableRange}}", request.getDisposableRange())
                .replace("{{existingSavings}}", request.getExistingSavings())
                .replace("{{lifeRhythm}}", request.getLifeRhythm())
                .replace("{{spendingHabit}}", request.getSpendingHabit())
                .replace("{{actionStyle}}", request.getActionStyle());
    }
}
