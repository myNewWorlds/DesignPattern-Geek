package com.ljt;

import com.ljt.rule.IDarkFeature;
import com.ljt.rule.UserPromotionDarkRule;

public class Demo {
    public static void main(String[] args) {
        // 默认加载classpath下dark-rule.yaml文件中的灰度规则
        DarkLaunch darkLaunch = new DarkLaunch();
        darkLaunch.addProgrammedDarkFeature("user_promotion", new UserPromotionDarkRule());
        IDarkFeature darkFeature = darkLaunch.getDarkFeature("user_promotion");
        System.out.println(darkFeature.enabled());
        System.out.println(darkFeature.dark(893));
    }
}
