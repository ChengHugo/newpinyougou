package com.pinyougou.cart.controller;

import com.alibaba.fastjson.JSONArray;
import com.pinyougou.common.util.CookieUtils;
import com.pinyougou.vo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/cart")
@RestController
public class CartController {

    //品优购系统的购物车在cookie中的名称
    private static final String COOKIE_CART_LIST = "PYG_CART_LIST";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    /**
     * 登录或者未登录情况下购物车列表的查询
     * @return 购物车列表
     */
    @GetMapping("/findCartList")
    public List<Cart> findCartList(){
        try {
            //因为配置了可以匿名访问所以如果是匿名访问的时候，返回的用户名为anonymousUser
            //如果未登录则用户名为：anonymousUser
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if ("anonymousUser".equals(username)) {
                //未登录；从cookie中获取购物车数据
                List<Cart> cookieCartList = new ArrayList<>();
                String cartListJsonStr = CookieUtils.getCookieValue(request, COOKIE_CART_LIST, true);
                if (!StringUtils.isEmpty(cartListJsonStr)) {
                   cookieCartList = JSONArray.parseArray(cartListJsonStr, Cart.class);
                }
                return cookieCartList;
            } else {
                //已登录，从redis中获取购物车数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取当前登录用户名
     * @return 用户信息
     */
    @GetMapping("/getUsername")
    public Map<String, Object> getUsername(){
        Map<String, Object> map = new HashMap<String, Object>();

        //因为配置了可以匿名访问所以如果是匿名访问的时候，返回的用户名为anonymousUser
        //如果未登录则用户名为：anonymousUser
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("username", username);
        return map;
    }
}
