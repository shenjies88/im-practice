package com.shenjies88.practice.im.backend.interceptor;

import com.shenjies88.practice.im.backend.manager.MyCacheManager;
import com.shenjies88.practice.im.backend.vo.authentication.LoginRespVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shenjies88
 * @since 2020/11/5-9:50 PM
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private static final String X_IM_TOKEN = "x-im-token";
    private final MyCacheManager cacheManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader(X_IM_TOKEN);
        Assert.hasText(token, "认证令牌不能为空");
        //token存活
        LoginRespVO vo = cacheManager.getToken(token);
        Assert.notNull(vo, "令牌已失效，请重新登录");
        String liveToken = cacheManager.getLiveToken(vo.getId());
        Assert.hasText(liveToken, "令牌已失效，请重新登录");
        //TODO 存入线程上下文
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //TODO 清除线程上下文
    }
}
