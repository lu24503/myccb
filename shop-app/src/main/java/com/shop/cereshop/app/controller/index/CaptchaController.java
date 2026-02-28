package com.shop.cereshop.app.controller.index;

import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.commons.utils.AppletPayUtil;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/code")
public class CaptchaController {

    @Autowired
    private StringRedisService stringRedisService;

    public static final String CAPTCHA_PREFIX = "captcha:app:";

    @GetMapping( "/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        try {
            // 创建GifCaptcha对象
            GifCaptcha gifCaptcha = new GifCaptcha(130, 48, 4);
            // 输出验证码图片
            CaptchaUtil.out(gifCaptcha, request, response);
            // 获取验证码文本
            String verCode = gifCaptcha.text().toLowerCase();
            // 将验证码文本保存到session中
            //request.getSession().setAttribute("CAPTCHA", verCode);

            String ip = AppletPayUtil.getClientIp(request);
            System.out.println("ip:" + ip);
            stringRedisService.set(CAPTCHA_PREFIX + ip, verCode);

            // 打印session ID
            System.out.println(request.getSession().getId());
        } catch (Exception e) {
            log.error("getCaptcha error: " + e.getMessage(), e);
        }
    }
}
