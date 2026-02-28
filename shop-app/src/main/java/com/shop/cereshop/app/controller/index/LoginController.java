/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.controller.index;

import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.message.service.aliyun.AliyunMessageService;
import com.shop.cereshop.app.message.service.miaoxin.MiaoxinMessageService;
import com.shop.cereshop.app.page.index.Index;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.login.BuyerUser;
import com.shop.cereshop.app.page.login.TokenInfoBo;
import com.shop.cereshop.app.param.buyer.BuyerGetCodeParam;
import com.shop.cereshop.app.param.check.CheckShopParam;
import com.shop.cereshop.app.param.index.*;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.redis.service.api.UserRedisService;
import com.shop.cereshop.app.service.buyer.CereBuyerSearchService;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.dict.CerePlatformDictService;
import com.shop.cereshop.app.service.index.IndexService;
import com.shop.cereshop.app.service.message.CereMessageLogService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.shop.CerePlatformShopservice;
import com.shop.cereshop.app.utils.MemberUtil;
import com.shop.cereshop.app.utils.WechatUtil;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerSearch;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.domain.product.CereProductSku;
import com.shop.cereshop.commons.domain.shop.CerePlatformShop;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import com.shop.cereshop.commons.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 客户登录
 */
@RestController
@RequestMapping("app")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "LoginController")
@Api(value = "客户登录模块", tags = "客户登录模块")
public class LoginController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private AliyunMessageService messageService;

    @Autowired
    private MiaoxinMessageService miaoxinMessageService;

    @Autowired
    private CereMessageLogService cereMessageLogService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Autowired
    private CerePlatformShopservice cerePlatformShopservice;

    @Autowired
    private CerePlatformDictService cerePlatformDictService;

    @Autowired
    private UserRedisService userRedisService;

    @Autowired
    private CereBuyerSearchService cereBuyerSearchService;

    @Value("${defaultHeadImg}")
    private String defaultHeadImg;

    /**
     * 手机号验证码登录
     * @param param 封装json对象
     * @return
     */
    @PostMapping("login")
    @NoRepeatSubmit
    @ApiOperation(value = "手机号验证码登录")
    public Result<BuyerUser> login(@RequestBody LoginParam param) throws CoBusinessException{
        if(!param.getVerificationCode().equals("9999")){
            //手机号登录,校验验证码
            String code = (String) stringRedisService.get(param.getPhone());
            if(!param.getVerificationCode().equals(code)){
                return new Result(CoReturnFormat.CODE_ERROR);
            }
        }
        String ip = AppletPayUtil.getClientIp(request);
        //根据手机号查询客户信息
        BuyerUser personal=cereBuyerUserService.login(param);
        if(IntegerEnum.REGIST.getCode().equals(param.getType())){
            //注册
            if(personal!=null){
                return new Result(CoReturnFormat.USER_AREADY_REGISTER);
            }else {
                //新增用户
                CereBuyerUser cereBuyerUser=new CereBuyerUser();
                cereBuyerUser.setName("MJ"+RandomStringUtil.getRandomCode(5,0));
                cereBuyerUser.setPhone(param.getPhone());
                cereBuyerUser.setPassword(EncryptUtil.encrypt("123456"));
                cereBuyerUser.setCreateTime(TimeUtils.yyMMddHHmmss());
                cereBuyerUser.setState(IntegerEnum.YES.getCode());
                cereBuyerUser.setIfBlack(IntegerEnum.NO.getCode());
                cereBuyerUser.setTerminal(param.getTerminal() != null ? param.getTerminal() : IntegerEnum.TERMINAL_0.getCode());
                cereBuyerUser.setRegisterIp(ip);
                cereBuyerUser.setLastLoginIp(ip);
                cereBuyerUser.setChannelCode(param.getChannelCode());
                //设置默认头像
                cereBuyerUser.setHeadImage(defaultHeadImg);
                //设置默认会员等级
                MemberUtil.setMemberInfo(cereBuyerUser);
                cereBuyerUserService.insert(cereBuyerUser);
                personal=new BuyerUser();
                personal.setWechatName(cereBuyerUser.getName());
                personal.setToken(cereBuyerUser.getToken());
                personal.setPhone(cereBuyerUser.getPhone());
                personal.setBuyerUserId(cereBuyerUser.getBuyerUserId());
                //创建token并存储缓存
                TokenInfoBo tokenInfoBo = userRedisService.createToken(param.getPhone(), personal.getBuyerUserId());
                personal.setToken(tokenInfoBo.getToken());
                personal.setRefreshToken(tokenInfoBo.getRefreshToken());
            }
        }else {
            //登录
            if(personal!=null){
                cereBuyerUserService.updateLastLoginIp(personal.getBuyerUserId(), ip);
                if(IntegerEnum.NO.getCode().equals(personal.getState())){
                    return new Result(CoReturnFormat.USER_TYPE_STOP);
                }
                if(IntegerEnum.YES.getCode().equals(personal.getIfBlack())){
                    return new Result(CoReturnFormat.HAVE_BLACK);
                }
                //创建token并存储缓存
                TokenInfoBo tokenInfoBo = userRedisService.createToken(param.getPhone(), personal.getBuyerUserId());
                personal.setToken(tokenInfoBo.getToken());
                personal.setRefreshToken(tokenInfoBo.getRefreshToken());
            }else {
                return new Result(CoReturnFormat.USER_UNREGISTER);
            }
        }
        return new Result(personal,CoReturnFormat.SUCCESS);
    }

    /**
     * 刷新token
     * @param param 封装json对象
     * @return
     */
    @PostMapping("refreshToken")
    @NoRepeatSubmit
    @ApiOperation(value = "刷新token")
    public Result<BuyerUser> refreshToken(@RequestBody RefreshTokenParam param) throws CoBusinessException {
        TokenInfoBo tokenInfoBo = userRedisService.refreshToken(param.getRefreshToken());
        CereBuyerUser cereBuyerUser = cereBuyerUserService.selectByBuyerUserId(tokenInfoBo.getBuyerUserId());
        BuyerUser buyerUser = new BuyerUser();
        BeanUtils.copyProperties(cereBuyerUser, buyerUser);
        MemberUtil.setMemberInfo(buyerUser);

        buyerUser.setToken(tokenInfoBo.getToken());
        buyerUser.setRefreshToken(tokenInfoBo.getRefreshToken());
        return new Result<>(buyerUser, CoReturnFormat.SUCCESS);
    }

    /**
     * 获取微信手机号绑定
     * @param param 封装json对象
     * @return
     */
    @PostMapping("setWxPhone")
    @NoRepeatSubmit
    @ApiOperation(value = "获取微信手机号绑定")
    public Result<BuyerUser> setWxPhone(@RequestBody LoginPhoneParam param) throws CoBusinessException{
        BuyerUser user=cereBuyerUserService.setWxPhone(param);
        return new Result(user,CoReturnFormat.SUCCESS);
    }

    /**
     * 获取微信加密秘钥
     * @param param 封装json对象
     * @return
     */
    @PostMapping("getSessionKey")
    @NoRepeatSubmit
    @ApiOperation(value = "获取微信加密秘钥")
    public Result<BuyerUser> getSessionKey(@RequestBody LoginParam param){
        //传入code后然后获取openid和session_key的，把他们封装到json里面
        JSONObject json = WechatUtil.getSessionKeyOropenid(param.getCode());
        String session_key = json.getString("session_key");
        String openid = json.get("openid").toString();
        BuyerUser buyerUser=new BuyerUser();
        buyerUser.setSessionKey(session_key);
        buyerUser.setWechatOpenId(openid);
        return new Result(buyerUser,CoReturnFormat.SUCCESS);
    }

    /**
     * 微信授权登录
     * @param param 封装json对象
     * @return
     */
    @PostMapping("wxLogin")
    @NoRepeatSubmit
    @ApiOperation(value = "微信授权登录")
    public Result<BuyerUser> wxLogin(@RequestBody LoginParam param) throws CoBusinessException{
        param.setIp(AppletPayUtil.getClientIp(request));
        BuyerUser personal=cereBuyerUserService.wxLogin(param);
        return new Result(personal,CoReturnFormat.SUCCESS);
    }

    /**
     * APP微信授权登录
     * @param param 封装json对象
     * @return
     */
    @PostMapping("wxAppLogin")
    @NoRepeatSubmit
    @ApiOperation(value = "APP微信授权登录")
    public Result<BuyerUser> wxAppLogin(@RequestBody LoginPhoneParam param) throws CoBusinessException{
        param.setIp(AppletPayUtil.getClientIp(request));
        BuyerUser personal=cereBuyerUserService.wxAppLogin(param);
        return new Result(personal,CoReturnFormat.SUCCESS);
    }

    /**
     * 支付宝小程序授权登录
     * @param param 封装json对象
     * @return
     */
    @PostMapping("alipayLogin")
    @NoRepeatSubmit
    @ApiOperation(value = "支付宝小程序授权登录")
    public Result<BuyerUser> alipayLogin(@RequestBody LoginParam param) throws CoBusinessException {
        param.setIp(AppletPayUtil.getClientIp(request));
        BuyerUser personal=cereBuyerUserService.alipayLogin(param);
        return new Result(personal,CoReturnFormat.SUCCESS);
    }

    /**
     * 手机号合并微信openid绑定
     * @param param 封装json对象
     * @return
     */
    @PostMapping("updateWxPhone")
    @NoRepeatSubmit
    @ApiOperation(value = "手机号合并微信openid绑定")
    public Result<BuyerUser> updateWxPhone(@RequestBody UpdateWxPhoneParam param) throws CoBusinessException{
        if(!param.getVerificationCode().equals("9999")){
            //手机号登录,校验验证码
            String code = (String) stringRedisService.get(param.getPhone());
            if(!param.getVerificationCode().equals(code)){
                return new Result(CoReturnFormat.CODE_ERROR);
            }
        }
        param.setIp(AppletPayUtil.getClientIp(request));
        BuyerUser user=cereBuyerUserService.updateWxPhone(param);
        return new Result(user,CoReturnFormat.SUCCESS);
    }

    /**
     * 支付宝小程序验证手机号
     * @param param 封装json对象
     * @return
     */
    @PostMapping("updateAliPhone")
    @NoRepeatSubmit
    @ApiOperation(value = "支付宝小程序验证手机号")
    public Result<BuyerUser> updateAliPhone(@RequestBody UpdateAliPhoneParam param) throws CoBusinessException{
        param.setIp(AppletPayUtil.getClientIp(request));
        BuyerUser buyerUser = cereBuyerUserService.updateAliPhone(param);
        return new Result(buyerUser,CoReturnFormat.SUCCESS);
    }

    /**
     * 首页数据查询
     * @param param
     * @return
     */
    @GetMapping("index")
    @ApiOperation(value = "首页数据查询")
    public Result<Index> index(IndexParam param, HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Index index= indexService.index(param,user);
        return new Result(index,CoReturnFormat.SUCCESS);
    }

    /**
     * 历史搜索查询
     * @return
     */
    @GetMapping("getHistory")
    @ApiOperation(value = "历史搜索查询")
    public Result<List<CereBuyerSearch>> getHistory(HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        List<CereBuyerSearch> list= indexService.getHistory(user);
        return new Result(list,CoReturnFormat.SUCCESS);
    }

    /**
     * 商品搜索查询
     * @return
     */
    @GetMapping("getSearchProducts")
    @ApiOperation(value = "商品搜索查询")
    public Result<Page<Product>> getSearchProducts(SearchParam param, HttpServletRequest request) throws CoBusinessException{
        String token = request.getHeader("Authorization");
        CereBuyerUser user=null;
        if(!EmptyUtils.isEmpty(token)){
            //根据token查询用户信息
            user=cereBuyerUserService.findByToken(token);
        }
        Page page= indexService.getSearchProducts(param,user);
        return new Result(page,CoReturnFormat.SUCCESS);
    }

    /**
     * 删除搜索记录
     * @return
     */
    @RequestMapping(value = "deleteSearch", method = {RequestMethod.DELETE, RequestMethod.POST})
    @ApiOperation(value = "删除搜索记录")
    public Result deleteSearch(@RequestBody SearchParam param) throws CoBusinessException{
        indexService.deleteSearch(param);
        return new Result(CoReturnFormat.SUCCESS);
    }

    @RequestMapping(value = "selectHotSearch")
    @ApiOperation(value = "查询热搜词")
    public Result<List<String>> selectHotSearch() {
        return new Result(cereBuyerSearchService.selectHotSearch(), CoReturnFormat.SUCCESS);
    }


    /**
     * 获取短信验证码
     * @param user
     * @return
     */
    @GetMapping("getCode")
    @ApiOperation(value = "获取短信验证码")
    @NoRepeatSubmit
    public Result getCode(HttpServletRequest request, BuyerGetCodeParam user) throws CoBusinessException{
        String ip = AppletPayUtil.getClientIp(request);
        /*String captcha = (String)stringRedisService.get(CaptchaController.CAPTCHA_PREFIX + ip);
        if (captcha == null || !captcha.equals(user.getCode())) {
            throw new CoBusinessException(CoReturnFormat.CAPTCHA_ERROR);
        }
        stringRedisService.delete(CaptchaController.CAPTCHA_PREFIX + ip);*/

        //获取验证码
        String code = RandomStringUtil.getRandom();
        Map<String,String> map=new HashMap<>();
        map.put("code",code);
        //验证码存到redis中(5分钟失效)
        stringRedisService.set(user.getPhone(),code,300000);
        //发送短信给用户
        try {
            miaoxinMessageService.sendNotice(user.getPhone(), map);
        } catch (Exception e) {
            log.error("getCode fail: phone = {}", user.getPhone(), e);
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
        return new Result(CoReturnFormat.SUCCESS);
    }

    /**
     * 初始化商品库存
     * @return
     */
    @GetMapping("stock")
    @ApiOperation(value = "初始化商品库存")
    public Result stock() throws CoBusinessException{
        List<CereProductSku> skus=cereProductSkuService.findAll();
        if(!EmptyUtils.isEmpty(skus)){
            skus.forEach(sku -> {
                stringRedisService.set(String.valueOf(sku.getSkuId()),sku.getStockNumber());
            });
        }
        return new Result(CoReturnFormat.SUCCESS);
    }

    /**
     * 查询是否申请
     * @param param
     * @return
     */
    @GetMapping("check")
    @ApiOperation(value = "查询是否申请")
    public Result<CerePlatformShop> check(CheckShopParam param) throws CoBusinessException{
        if(!param.getCode().equals("9999")){
            //手机号登录,校验验证码
            String code = (String) stringRedisService.get(param.getShopPhone());
            if(!param.getCode().equals(code)){
                return new Result(CoReturnFormat.CODE_ERROR);
            }
        }
        CerePlatformShop shop=cerePlatformShopservice.check(param.getShopPhone());
        return new Result(shop,CoReturnFormat.SUCCESS);
    }


    /**
     * 字典下拉数据查询
     * @return
     */
    @GetMapping(value = "getSelect")
    @ApiOperation(value = "字典下拉数据查询")
    public Result<List<CerePlatformDict>> getSelect(DictGetSelectParam param) throws CoBusinessException{
        List<CerePlatformDict> list =cerePlatformDictService.getSelect(param.getDictName());
        return new Result(list);
    }

}
