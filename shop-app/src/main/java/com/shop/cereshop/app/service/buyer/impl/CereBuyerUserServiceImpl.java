/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.buyer.CereBuyerTrackReportDAO;
import com.shop.cereshop.app.dao.buyer.CereBuyerUserDAO;
import com.shop.cereshop.app.dao.credit.CereCreditRecordDAO;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.app.dao.recommend.CereRecommendCommentDAO;
import com.shop.cereshop.app.page.buyer.MyUser;
import com.shop.cereshop.app.page.distributor.Distributor;
import com.shop.cereshop.app.page.login.BuyerUser;
import com.shop.cereshop.app.page.login.TokenInfoBo;
import com.shop.cereshop.app.page.order.OrderCountDTO;
import com.shop.cereshop.app.page.order.Orders;
import com.shop.cereshop.app.page.seckill.SeckillProductAnswer;
import com.shop.cereshop.app.page.seckill.SeckillProductProblem;
import com.shop.cereshop.app.param.buyer.UserParam;
import com.shop.cereshop.app.param.index.LoginParam;
import com.shop.cereshop.app.param.index.LoginPhoneParam;
import com.shop.cereshop.app.param.index.UpdateAliPhoneParam;
import com.shop.cereshop.app.param.index.UpdateWxPhoneParam;
import com.shop.cereshop.app.param.order.OrderGetAllParam;
import com.shop.cereshop.app.redis.service.api.UserRedisService;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.after.CereOrderAfterService;
import com.shop.cereshop.app.service.buyer.*;
import com.shop.cereshop.app.service.cart.CereShopCartService;
import com.shop.cereshop.app.service.collect.CereBuyerCollectService;
import com.shop.cereshop.app.service.collect.CereBuyerFootprintService;
import com.shop.cereshop.app.service.distributor.CereDistributionOrderService;
import com.shop.cereshop.app.service.distributor.CereDistributorBuyerService;
import com.shop.cereshop.app.service.groupwork.CereCollageOrderService;
import com.shop.cereshop.app.service.notice.CereNoticeService;
import com.shop.cereshop.app.service.order.CereOrderProductService;
import com.shop.cereshop.app.service.order.CereShopOrderService;
import com.shop.cereshop.app.service.seckill.CereProductAnswerService;
import com.shop.cereshop.app.service.seckill.CereProductProblemService;
import com.shop.cereshop.app.service.shop.CereShopCommentService;
import com.shop.cereshop.app.service.shop.CereShopVisitService;
import com.shop.cereshop.app.utils.AlipayUtil;
import com.shop.cereshop.app.utils.MemberUtil;
import com.shop.cereshop.app.utils.WechatUtil;
import com.shop.cereshop.commons.cache.constants.CacheKeyConstants;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.CreditOptTypeEnum;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerTrackReport;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.canvas.CerePlatformCanvas;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.credit.CereCreditRecord;
import com.shop.cereshop.commons.domain.express.HttpResult;
import com.shop.cereshop.commons.domain.order.CereOrderProduct;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "CereBuyerUserServiceImpl")
public class CereBuyerUserServiceImpl implements CereBuyerUserService {
    @Autowired
    private CereDistributionOrderService cereDistributionOrderService;

    @Autowired
    private CereBuyerUserDAO cereBuyerUserDAO;

    @Autowired
    private CereProductProblemService cereProductProblemService;

    @Autowired
    private CereProductAnswerService cereProductAnswerService;

    @Autowired
    private CereBuyerBankService cereBuyerBankService;

    @Autowired
    private CereBuyerCollectService cereBuyerCollectService;

    @Autowired
    private CereBuyerCommentLikeService cereBuyerCommentLikeService;

    @Autowired
    private CereBuyerCouponService cereBuyerCouponService;

    @Autowired
    private CereBuyerShopCouponService cereBuyerShopCouponService;

    @Autowired
    private CereBuyerFootprintService cereBuyerFootprintService;

    @Autowired
    private CereBuyerLabelService cereBuyerLabelService;

    @Autowired
    private CereBuyerReceiveService cereBuyerReceiveService;

    @Autowired
    private CereBuyerSearchService cereBuyerSearchService;

    @Autowired
    private CereBuyerShopLabelService cereBuyerShopLabelService;

    @Autowired
    private CereBuyerWithdrawalService cereBuyerWithdrawalService;

    @Autowired
    private CereCollageOrderService cereCollageOrderService;

    @Autowired
    private CereDistributorBuyerService cereDistributorBuyerService;

    @Autowired
    private CereNoticeService cereNoticeService;

    @Autowired
    private CereShopCommentService cereShopCommentService;

    @Autowired
    private CereShopVisitService cereShopVisitService;

    @Autowired
    private CereShopCartService cereShopCartService;

    @Autowired
    private CereShopOrderService cereShopOrderService;

    @Autowired
    private UserRedisService userRedisService;

    @Autowired
    private CereShopOrderDAO cereShopOrderDAO;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CereCreditRecordDAO cereCreditRecordDAO;

    @Autowired
    private CereOrderProductService cereOrderProductService;

    @Autowired
    private CereBuyerTrackReportDAO cereBuyerTrackReportDAO;

    @Autowired
    private CereOrderAfterService cereOrderAfterService;

    @Value("${defaultHeadImg}")
    private String defaultHeadImg;

    @Value("${scrmTrackReportUrl}")
    private String scrmTrackReportUrl;

    @Autowired
    private CereRecommendCommentDAO cereRecommendCommentDAO;

    @Override
    public CereBuyerUser findByToken(String token) {
        Long buyerUserId = userRedisService.getBuyerUserIdByToken(token);
        if (buyerUserId != null) {
            CereBuyerUser user = cereBuyerUserDAO.selectByPrimaryKey(buyerUserId);
            user.setToken(token);
            return user;
        }
        return null;
    }

    @Override
    public CereBuyerUser selectByBuyerUserId(Long buyerUserId) {
        CereBuyerUser cereBuyerUser = cereBuyerUserDAO.selectByPrimaryKey(buyerUserId);
        if (cereBuyerUser != null && cereBuyerUser.getWechatName() == null) {
            cereBuyerUser.setWechatName(cereBuyerUser.getName());
        }
        return cereBuyerUser;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public BuyerUser wxLogin(LoginParam param) throws CoBusinessException {
        BuyerUser user=null;
        String time = TimeUtils.yyMMddHHmmss();
        //根据微信code获取unionId
        //log.info("code: " + param.getCode());
        JSONObject wx = WechatUtil.getOpenId(param.getCode());
        String openid=wx.get("openid").toString();
        String unionId="";
        if(!EmptyUtils.isEmpty(wx.get("unionid"))){
            unionId=wx.get("unionid").toString();
        }
        //查询该微信号是否已注册
        if(!EmptyUtils.isEmpty(openid)){
            user=cereBuyerUserDAO.findByOpenid(openid);
            if(user==null){
                //未注册
                user = new BuyerUser();
            }else {
                if(IntegerEnum.YES.getCode().equals(user.getIfBlack())){
                    throw new CoBusinessException(CoReturnFormat.HAVE_BLACK);
                }
                if (!EmptyUtils.isEmpty(param.getHeadImage())) {
                    //更新微信头像
                    CereBuyerUser cereBuyerUser=new CereBuyerUser();
                    cereBuyerUser.setBuyerUserId(user.getBuyerUserId());
                    cereBuyerUser.setHeadImage(param.getHeadImage());
                    cereBuyerUserDAO.updateByPrimaryKeySelective(cereBuyerUser);
                }
                updateLastLoginIp(user.getBuyerUserId(), param.getIp());
            }
            if (EmptyUtils.isEmpty(user.getPhone())) {
                user.setIfFirst(IntegerEnum.YES.getCode());
            } else {
                user.setIfFirst(IntegerEnum.NO.getCode());
                //设置token
                assembleToken(user);
            }
            user.setWechatOpenId(openid);
            MemberUtil.setMemberInfo(user);
            return user;
        }else {
            throw new CoBusinessException(CoReturnFormat.WECHAT_ERROR);
        }
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public BuyerUser alipayLogin(LoginParam param) throws CoBusinessException {
        BuyerUser user=null;
        String time = TimeUtils.yyMMddHHmmss();
        AlipayUserInfoShareResponse userInfoResponse = AlipayUtil.getBuyerId(param.getCode());
        if (userInfoResponse == null || StringUtils.isBlank(userInfoResponse.getUserId())) {
            throw new CoBusinessException(CoReturnFormat.ALI_APPLET_AUTH_ERROR);
        }
        user=cereBuyerUserDAO.findByAliUserId(userInfoResponse.getUserId());
        if (user==null) {
            //未注册
            user = new BuyerUser();
            user.setIfFirst(IntegerEnum.NO.getCode());
            user.setWechatName(userInfoResponse.getNickName());

            //新增用户
            CereBuyerUser buyerUser=new CereBuyerUser();
            //buyerUser.setWechatOpenId(param.getWechatOpenId());
            buyerUser.setAliUserId(userInfoResponse.getUserId());
            buyerUser.setCreateTime(time);
            buyerUser.setPhone(param.getPhone());
            buyerUser.setPassword(EncryptUtil.encrypt("123456"));
            String name = "MJ"+RandomStringUtil.getRandomCode(4,0);
            if (StringUtils.isNotBlank(userInfoResponse.getNickName())) {
                name = userInfoResponse.getNickName();
            }
            buyerUser.setName(name);
            buyerUser.setState(IntegerEnum.YES.getCode());
            buyerUser.setTerminal(IntegerEnum.TERMINAL_4.getCode());
            //设置头像
            if(!EmptyUtils.isEmpty(userInfoResponse.getAvatar())){
                buyerUser.setHeadImage(userInfoResponse.getAvatar());
            }else {
                buyerUser.setHeadImage(defaultHeadImg);
            }
            user.setHeadImage(buyerUser.getHeadImage());
            //设置默认会员等级
            MemberUtil.setMemberInfo(buyerUser);
            buyerUser.setChannelCode(param.getChannelCode());
            cereBuyerUserDAO.insert(buyerUser);
            user.setBuyerUserId(buyerUser.getBuyerUserId());
            user.setAliUserId(userInfoResponse.getUserId());
            //user.setToken(buyerUser.getToken());
        } else {
            if(IntegerEnum.YES.getCode().equals(user.getIfBlack())){
                throw new CoBusinessException(CoReturnFormat.HAVE_BLACK);
            }
            if (!EmptyUtils.isEmpty(param.getHeadImage())) {
                //更新微信头像
                CereBuyerUser cereBuyerUser=new CereBuyerUser();
                cereBuyerUser.setBuyerUserId(user.getBuyerUserId());
                cereBuyerUser.setHeadImage(param.getHeadImage());
                cereBuyerUserDAO.updateByPrimaryKeySelective(cereBuyerUser);
            }
            updateLastLoginIp(user.getBuyerUserId(), param.getIp());
        }
        if (StringUtils.isBlank(user.getPhone())) {
            user.setIfFirst(IntegerEnum.YES.getCode());
        }

        assembleToken(user);

        user.setAliUserId(userInfoResponse.getUserId());
        MemberUtil.setMemberInfo(user);
        return user;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public BuyerUser wxAppLogin(LoginPhoneParam param) throws CoBusinessException {
        BuyerUser user=null;
        String time = TimeUtils.yyMMddHHmmss();
        //查询该微信号是否已注册
        if(!EmptyUtils.isEmpty(param.getWechatOpenId())){
            user=cereBuyerUserDAO.findByOpenid(param.getWechatOpenId());
            if(user==null){
                //未注册
                user = new BuyerUser();
                user.setIfFirst(IntegerEnum.YES.getCode());
            }else {
                user.setIfFirst(IntegerEnum.NO.getCode());
                assembleToken(user);
                updateLastLoginIp(user.getBuyerUserId(), param.getIp());
            }
            MemberUtil.setMemberInfo(user);
            return user;
        }else {
            throw new CoBusinessException(CoReturnFormat.WECHAT_ERROR);
        }
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public BuyerUser setWxPhone(LoginPhoneParam param) throws CoBusinessException {
        Map<String,Object> map = WechatUtil.getPhoneNumber(param);
        if(EmptyUtils.isEmpty(map)){
            throw new CoBusinessException(CoReturnFormat.WECHAT_ERROR);
        }
        String phone="";
        Object phoneNumber = map.get("param");
        String jsonString = JSONObject.toJSONString(phoneNumber);
        JSONObject obj=JSONObject.parseObject(jsonString);
        if(!EmptyUtils.isEmpty(jsonString)) {
            phone = obj.get("phoneNumber").toString();
        }
        BuyerUser result=new BuyerUser();
        result.setPhone(phone);
        String time =TimeUtils.yyMMddHHmmss();
        //校验手机号是否绑定其他微信
        CereBuyerUser cereBuyerUser=cereBuyerUserDAO.checkPhoneWx(phone);
        if(cereBuyerUser!=null){
            //绑定了微信
            if(!cereBuyerUser.getWechatOpenId().equals(param.getWechatOpenId())){
                //如果和当前微信openid不一致，提示
                throw new CoBusinessException(CoReturnFormat.PHONE_ALREADY_WX);
            }else {
                //如果一致,直接返回用户信息
                result.setBuyerUserId(cereBuyerUser.getBuyerUserId());
                result.setHeadImage(cereBuyerUser.getHeadImage());
                result.setWechatOpenId(param.getWechatOpenId());
                result.setWechatName(cereBuyerUser.getWechatName());

                assembleToken(result);

                MemberUtil.setMemberInfo(result);
                return result;
            }
        }
        //查询手机号已注册用户数据
        BuyerUser byPhone = cereBuyerUserDAO.findByPhone(phone);
        //查询微信已注册用户数据
        CereBuyerUser user=cereBuyerUserDAO.getByOpenid(param.getWechatOpenId());
        if(byPhone!=null){
            if(user!=null){
                //删除手机号注册用户
                cereBuyerUserDAO.deleteByPrimaryKey(byPhone.getBuyerUserId());
                //更新微信注册用户绑定手机号
                user.setPhone(byPhone.getPhone());
                user.setUpdateTime(time);
                user.setHeadImage(param.getHeadImage());
                cereBuyerUserDAO.updateByPrimaryKeySelective(user);
                result.setBuyerUserId(user.getBuyerUserId());
                result.setWechatOpenId(param.getWechatOpenId());
                //result.setToken(user.getToken());
                result.setPhone(phone);
                //同步更新手机号注册的业务数据转移到微信注册用户
                transferUserData(byPhone.getBuyerUserId(),user.getBuyerUserId());
            }else {
                //更新手机号已注册用户openid
                CereBuyerUser buyerUser=new CereBuyerUser();
                buyerUser.setBuyerUserId(byPhone.getBuyerUserId());
                buyerUser.setWechatOpenId(param.getWechatOpenId());
                buyerUser.setUpdateTime(time);
                //设置头像
                buyerUser.setHeadImage(param.getHeadImage());
                cereBuyerUserDAO.updateByPrimaryKeySelective(buyerUser);
                result=byPhone;
            }
        }else {
            //未绑定其他微信,更新手机号
            if(user!=null){
                user.setPhone(phone);
                user.setUpdateTime(time);
                user.setHeadImage(param.getHeadImage());
                cereBuyerUserDAO.updateByPrimaryKeySelective(user);
                result.setBuyerUserId(user.getBuyerUserId());
                result.setWechatOpenId(param.getWechatOpenId());
                //result.setToken(user.getToken());
            }else {
                //新增用户
                CereBuyerUser buyerUser=new CereBuyerUser();
                buyerUser.setWechatOpenId(param.getWechatOpenId());
                buyerUser.setCreateTime(time);
                buyerUser.setPhone(phone);
                buyerUser.setPassword(EncryptUtil.encrypt("123456"));
                buyerUser.setName("MJ"+RandomStringUtil.getRandomCode(4,0));
                buyerUser.setState(IntegerEnum.YES.getCode());
                buyerUser.setTerminal(IntegerEnum.TERMINAL_2.getCode());
                //设置头像
                if(!EmptyUtils.isEmpty(param.getHeadImage())){
                    buyerUser.setHeadImage(param.getHeadImage());
                }else {
                    buyerUser.setHeadImage(defaultHeadImg);
                }
                //设置默认会员等级
                MemberUtil.setMemberInfo(buyerUser);
                buyerUser.setChannelCode(param.getChannelCode());
                cereBuyerUserDAO.insert(buyerUser);
                result.setBuyerUserId(buyerUser.getBuyerUserId());
                result.setWechatOpenId(param.getWechatOpenId());
            }
        }
        assembleToken(result);
        MemberUtil.setMemberInfo(result);
        return result;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public BuyerUser updateWxPhone(UpdateWxPhoneParam param) throws CoBusinessException {
        BuyerUser result=new BuyerUser();
        result.setPhone(param.getPhone());
        String time =TimeUtils.yyMMddHHmmss();
        //校验手机号是否绑定其他微信
        CereBuyerUser cereBuyerUser=cereBuyerUserDAO.checkPhoneWx(param.getPhone());
        if(cereBuyerUser!=null){
            throw new CoBusinessException(CoReturnFormat.PHONE_ALREADY_WX);
        }
        //查询手机号已注册用户数据
        BuyerUser byPhone = cereBuyerUserDAO.findByPhone(param.getPhone());
        //查询微信已注册用户数据
        CereBuyerUser user=cereBuyerUserDAO.getByOpenid(param.getWechatOpenId());
        if(byPhone!=null){
            if(user!=null){
                //删除手机号注册用户
                cereBuyerUserDAO.deleteByPrimaryKey(byPhone.getBuyerUserId());
                //更新微信注册用户绑定手机号
                user.setPhone(byPhone.getPhone());
                user.setUpdateTime(time);
                user.setHeadImage(param.getHeadImage());
                cereBuyerUserDAO.updateByPrimaryKeySelective(user);
                result.setBuyerUserId(user.getBuyerUserId());
                result.setWechatOpenId(param.getWechatOpenId());
                //result.setToken(user.getToken());
                result.setPhone(param.getPhone());
                //迁移用户数据
                transferUserData(byPhone.getBuyerUserId(),user.getBuyerUserId());
            }else {
                //更新手机号已注册用户openid
                CereBuyerUser buyerUser=new CereBuyerUser();
                buyerUser.setBuyerUserId(byPhone.getBuyerUserId());
                buyerUser.setWechatOpenId(param.getWechatOpenId());
                buyerUser.setUpdateTime(time);
                //设置头像
                buyerUser.setHeadImage(param.getHeadImage());
                cereBuyerUserDAO.updateByPrimaryKeySelective(buyerUser);
                result=byPhone;
            }
        }else {
            //未绑定其他微信,更新手机号
            if(user!=null){
                user.setPhone(param.getPhone());
                user.setUpdateTime(time);
                user.setHeadImage(param.getHeadImage());
                cereBuyerUserDAO.updateByPrimaryKeySelective(user);
                result.setBuyerUserId(user.getBuyerUserId());
                result.setWechatOpenId(param.getWechatOpenId());
                //result.setToken(user.getToken());
            }else {
                //新增用户
                CereBuyerUser buyerUser=new CereBuyerUser();
                buyerUser.setWechatOpenId(param.getWechatOpenId());
                buyerUser.setCreateTime(time);
                buyerUser.setPhone(param.getPhone());
                //buyerUser.setToken(RandomStringUtil.getRandomCode(32,0));
                buyerUser.setPassword(EncryptUtil.encrypt("123456"));
                buyerUser.setName("MJ"+RandomStringUtil.getRandomCode(4,0));
                buyerUser.setState(IntegerEnum.YES.getCode());
                buyerUser.setTerminal(IntegerEnum.TERMINAL_2.getCode());
                //设置头像
                if(!EmptyUtils.isEmpty(param.getHeadImage())){
                    buyerUser.setHeadImage(param.getHeadImage());
                }else {
                    buyerUser.setHeadImage(defaultHeadImg);
                }
                //设置默认会员等级
                MemberUtil.setMemberInfo(buyerUser);
                buyerUser.setRegisterIp(param.getIp());
                buyerUser.setChannelCode(param.getChannelCode());
                cereBuyerUserDAO.insert(buyerUser);
                result.setBuyerUserId(buyerUser.getBuyerUserId());
                result.setWechatOpenId(param.getWechatOpenId());
                //result.setToken(buyerUser.getToken());
            }
        }

        assembleToken(result);

        MemberUtil.setMemberInfo(result);
        return result;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public BuyerUser updateAliPhone(UpdateAliPhoneParam param) throws CoBusinessException {
        String phone = param.getPhone();
        if (StringUtils.isNotBlank(phone) && param.getEncrypted() != null && param.getEncrypted()) {
            try {
                //phone = "15622728105";
                JSONObject obj = PayUtil.decryptedData(phone);
                log.info("decryptedData: " + obj.toJSONString());
                if ("10000".equals(obj.getString("code"))) {
                    phone = obj.getString("mobile");
                } else {
                    throw new CoBusinessException(CoReturnFormat.VERIFY_PHONE_ERROR);
                }
            } catch (Exception e) {
                log.error("updateAliPhone failed: " + e.getMessage(),e);
                throw new CoBusinessException(CoReturnFormat.VERIFY_PHONE_ERROR);
            }
        }
        String time = TimeUtils.yyMMddHHmmss();

        CereBuyerUser existUser = cereBuyerUserDAO.checkPhoneAli(phone,param.getBuyerUserId());
        if (existUser != null) {
            throw new CoBusinessException(CoReturnFormat.OTHER_ALI_USER_BIND_PHONE);
        }

        try {
            //查询手机号已注册用户数据
            CereBuyerUser user = cereBuyerUserDAO.selectByPrimaryKey(param.getBuyerUserId());
            CereBuyerUser byPhone = cereBuyerUserDAO.selectByPhone(phone);
            if(byPhone!=null && !byPhone.getBuyerUserId().equals(param.getBuyerUserId())){
                //删除手机号注册用户
                cereBuyerUserDAO.deleteByPrimaryKey(byPhone.getBuyerUserId());
                //迁移用户数据
                transferUserData(byPhone.getBuyerUserId(),param.getBuyerUserId());
                //同步微信相关的信息
                //如果当前新账户还没有微信信息，同步过来
                if (StringUtils.isBlank(user.getWechatOpenId())) {
                    user.setWechatOpenId(byPhone.getWechatOpenId());
                }
                if (StringUtils.isBlank(user.getWechatUnionId())) {
                    user.setWechatUnionId(byPhone.getWechatUnionId());
                }
                if (StringUtils.isBlank(user.getWechatName())) {
                    user.setWechatName(byPhone.getWechatName());
                }
                if (StringUtils.isBlank(user.getWechatNumber())) {
                    user.setWechatNumber(byPhone.getWechatNumber());
                }
            }
            user.setPhone(phone);
            user.setUpdateTime(time);
            cereBuyerUserDAO.updateByPrimaryKeySelective(user);
            BuyerUser result = cereBuyerUserDAO.findByPhone(phone);

            assembleToken(result);

            MemberUtil.setMemberInfo(result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new CoBusinessException(CoReturnFormat.VERIFY_PHONE_ERROR);
        }
    }

    @Override
    public void updateGrowth(Long buyerUserId, Integer growth) {
        CereBuyerUser cereBuyerUser = cereBuyerUserDAO.selectByPrimaryKey(buyerUserId);
        if (cereBuyerUser != null) {
            //退款的时候，成长值为负，不用降会员等级
            if (growth < 0) {
                cereBuyerUserDAO.updateGrowth(buyerUserId, growth);
            } else {
                MemberUtil.updateMemberInfo(cereBuyerUser, growth);
                cereBuyerUserDAO.updateByPrimaryKey(cereBuyerUser);
            }
        }
    }

    @Override
    public int increaseCredit(Long buyerUserId, Integer credit, CreditOptTypeEnum typeEnum) {
        if (credit <= 0) {
            return 0;
        }
        //定义加锁key
        String key = CacheKeyConstants.CREDIT_LOCK + buyerUserId;
        //加锁
        RLock redissonLock = redissonClient.getLock(key);
        String time = TimeUtils.yyMMddHHmmss();
        try {
            redissonLock.lock();
            cereBuyerUserDAO.increaseCredit(buyerUserId, credit);
            //增加积分记录
            CereCreditRecord record = CreditOptTypeEnum.initRecord(typeEnum);
            record.setBuyerUserId(buyerUserId);
            record.setCreateTime(time);
            record.setUpdateTime(time);
            record.setCredit(Math.abs(credit));

            CereBuyerUser user = cereBuyerUserDAO.selectById(buyerUserId);
            record.setRemainCredit(user.getCredit());
            cereCreditRecordDAO.insert(record);
            return 1;
        } catch (Exception e) {
            log.error("increaseCredit fail: buyerUserId = {}, credit = {}" + e.getMessage(),
                    buyerUserId, credit, e);
        } finally {
            redissonLock.unlock();
        }
        return 0;
    }

    @Override
    public int decreaseCredit(Long buyerUserId, Integer credit, CreditOptTypeEnum typeEnum) throws CoBusinessException {
        if (credit <= 0) {
            return 0;
        }
        //定义加锁key
        String key = CacheKeyConstants.CREDIT_LOCK + buyerUserId;
        //加锁
        RLock redissonLock = redissonClient.getLock(key);
        String time = TimeUtils.yyMMddHHmmss();
        try {
            redissonLock.lock();
            CereBuyerUser buyerUser = cereBuyerUserDAO.selectById(buyerUserId);
            if (buyerUser.getCredit() == null || buyerUser.getCredit().compareTo(credit) < 0) {
                throw new CoBusinessException(CoReturnFormat.CREDIT_NOT_ENOUGH);
            }
            cereBuyerUserDAO.decreaseCredit(buyerUserId, credit);
            //增加积分记录
            CereCreditRecord record = CreditOptTypeEnum.initRecord(typeEnum);
            record.setBuyerUserId(buyerUserId);
            record.setCreateTime(time);
            record.setUpdateTime(time);
            record.setCredit(credit);

            record.setRemainCredit(buyerUser.getCredit() - credit);
            cereCreditRecordDAO.insert(record);
            return 1;
        } catch (CoBusinessException cbe) {
            throw cbe;
        } catch (Exception e) {
            log.error("increaseCredit fail: buyerUserId = {}, credit = {}" + e.getMessage(),
                    buyerUserId, credit, e);
        } finally {
            redissonLock.unlock();
        }
        return 0;
    }

    @Override
    public void rollbackCreditByOrderId(Long orderId, Long buyerUserId) {
        //拿到商品库存数据
        List<CereOrderProduct> opList = cereOrderProductService.findByOrderIds(Collections.singletonList(orderId));
        int sum = opList.stream().filter(op->op.getUseCredit() != null && op.getUseCredit() > 0).mapToInt(CereOrderProduct::getUseCredit).sum();
        if (sum > 0) {
            increaseCredit(buyerUserId, sum, CreditOptTypeEnum.ORDER_ROLLBACK_CREDIT);
        }
    }

    @Override
    public void delete(CereBuyerUser user) throws CoBusinessException {
        // 查订单，1.账户近期不存在交易:您的账户无未完成订单、无已完成但未满15天订单。
        Integer[] states = new Integer[]{1,2,3};
        List<Orders> list = cereShopOrderDAO.getAllInState(states,user.getBuyerUserId());
        if(list.size()>0){
            throw new CoBusinessException("您的账户存在待付款/已付款未完成订单");
        }

        // 已完成订单，但未满15天
        /*OrderGetAllParam orderGetAllParam = new OrderGetAllParam();
        orderGetAllParam.setBuyerUserId(user.getBuyerUserId());
        orderGetAllParam.setState("4");*/
        List<CereShopOrder> completedList = cereShopOrderDAO.selectList(Wrappers.<CereShopOrder>lambdaQuery().eq(CereShopOrder::getBuyerUserId, user.getBuyerUserId())
                .eq(CereShopOrder::getState, IntegerEnum.ORDER_FINISH.getCode()));
        // 校验时间
        for (CereShopOrder order:completedList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date parse = simpleDateFormat.parse(order.getCreateTime());
                long createTime = parse.getTime();
                if(createTime + ( 24 * 60 * 60 * 1000 * 15 )>new Date().getTime()){
                    throw new CoBusinessException("您的账户存在已完成订单，但是未满15天");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //2.账户相关财产权益已结清:您的账户不存在可提现的余额。
        Distributor distributor=cereDistributionOrderService.getDistributor(user.getBuyerUserId());
        if(!ObjectUtil.isNull(distributor.getPrice())&&distributor.getPrice().equals(BigDecimal.ZERO)){
            throw new CoBusinessException("您的账户存在存在可提现的余额");
        }

        // 删除账户
        int i = cereBuyerUserDAO.deleteByPrimaryKey(user.getBuyerUserId());
        if(i<=0){
            throw new CoBusinessException("数据异常");
        }
    }

    @Override
    public boolean trackReport(Long buyerUserId, Integer eventType, String productIds) {
        CereBuyerTrackReport report = new CereBuyerTrackReport();
        report.setEventType(eventType);
        report.setProductIds(productIds);
        report.setCreateTime(TimeUtils.yyMMddHHmmss());
        report.setCreateUser(buyerUserId);
        int count = cereBuyerTrackReportDAO.insert(report);

        CereBuyerUser buyerUser = cereBuyerUserDAO.selectById(buyerUserId);
        if (buyerUser == null) {
            return false;
        }

        JSONObject obj = new JSONObject();
        obj.put("mobile", buyerUser.getPhone());
        obj.put("nickName", StringUtils.isNotBlank(buyerUser.getName()) ? buyerUser.getName() : buyerUser.getWechatName());
        int sex = 2;
        if (buyerUser.getSex() != null && "男".equals(buyerUser.getSex())) {
            sex = 0;
        } else if (buyerUser.getSex() != null && "女".equals(buyerUser.getSex())) {
            sex = 1;
        }
        obj.put("sex", sex);
        obj.put("brithDay", buyerUser.getBirthday());
        obj.put("eventType", eventType);

        // 暂不处理埋点上传
        /*HttpResult result = HttpUtils.doPostJson(scrmTrackReportUrl, obj.toJSONString());
        if (result.getStatus() == 200) {
            log.info("trackReport success");
        } else {
            log.error("trackReport error");
        }*/

        return count > 0;
    }

    @Override
    public void updateLastLoginIp(Long buyerUserId, String ip) {
        CereBuyerUser user = new CereBuyerUser();
        user.setBuyerUserId(buyerUserId);
        user.setLastLoginIp(ip);
        cereBuyerUserDAO.updateById(user);
    }

    /**
     * 迁移用户数据
     * @param srcUserId
     * @param targetUserId
     */
    private void transferUserData(Long srcUserId, Long targetUserId) throws CoBusinessException{
        //同步更新手机号注册的业务数据转移到微信注册用户
        //银行卡数据转移
        cereBuyerBankService.updateBuyerData(targetUserId,srcUserId);
        //收藏数据转移
        cereBuyerCollectService.updateBuyerData(targetUserId,srcUserId);
        //是否点赞数据转移
        cereBuyerCommentLikeService.updateBuyerData(targetUserId,srcUserId);
        //领取优惠券数据转移
        cereBuyerCouponService.updateBuyerData(targetUserId,srcUserId);
        cereBuyerShopCouponService.updateBuyerData(targetUserId,srcUserId);
        //浏览足迹数据转移
        cereBuyerFootprintService.updateBuyerData(targetUserId,srcUserId);
        //关联平台标签数据转移
        cereBuyerLabelService.updateBuyerData(targetUserId,srcUserId);
        //收货地址数据转移
        cereBuyerReceiveService.updateBuyerData(targetUserId,srcUserId);
        //搜索记录数据转移
        cereBuyerSearchService.updateBuyerData(targetUserId,srcUserId);
        //关联店铺标签数据转移
        cereBuyerShopLabelService.updateBuyerData(targetUserId,srcUserId);
        //提现申请数据转移
        cereBuyerWithdrawalService.updateBuyerData(targetUserId,srcUserId);
        //拼团单数据转移
        cereCollageOrderService.updateBuyerData(targetUserId,srcUserId);
        //分销员关系数据转移
        cereDistributorBuyerService.updateBuyerData(targetUserId,srcUserId);
        //消息数据转移
        cereNoticeService.updateBuyerData(targetUserId,srcUserId);
        //回答数据转移
        cereProductAnswerService.updateBuyerData(targetUserId,srcUserId);
        //提问数据转移
        cereProductProblemService.updateBuyerData(targetUserId,srcUserId);
        //购物车数据转移
        cereShopCartService.updateBuyerData(targetUserId,srcUserId);
        //评论数据转移
        cereShopCommentService.updateBuyerData(targetUserId,srcUserId);
        //订单数据转移
        cereShopOrderService.updateBuyerData(targetUserId,srcUserId);
        //店铺浏览数据转移
        cereShopVisitService.updateBuyerData(targetUserId,srcUserId);
    }

    @Override
    public BuyerUser login(LoginParam param) throws CoBusinessException {
        BuyerUser user = cereBuyerUserDAO.findByPhone(param.getPhone());
        MemberUtil.setMemberInfo(user);
        return user;
    }

    @Override
    public void insert(CereBuyerUser buyerUser) throws CoBusinessException {
        cereBuyerUserDAO.insert(buyerUser);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void update(CereBuyerUser buyerUser, CereBuyerUser user) throws CoBusinessException {
        cereBuyerUserDAO.updateByPrimaryKeySelective(buyerUser);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void updatePassword(UserParam param, CereBuyerUser user) throws CoBusinessException {
        CereBuyerUser cereBuyerUser=new CereBuyerUser();
        cereBuyerUser.setBuyerUserId(user.getBuyerUserId());
        cereBuyerUser.setPassword(BCrypt.hashpw(param.getNewPassword(), BCrypt.gensalt(13)));
        cereBuyerUserDAO.updateByPrimaryKeySelective(cereBuyerUser);
    }

    @Override
    public MyUser getUser(Long buyerUserId) throws CoBusinessException {
        MyUser user = cereBuyerUserDAO.getUser(buyerUserId);
        if(user!=null){
            //查询未读消息数量
            int notRead = cereNoticeService.getNotRead(buyerUserId,user.getCreateTime());
            //查询公告、站内信未读消息数量
            //int read=cereNoticeService.getRead(buyerUserId);
            user.setNotRead(notRead);
            // 设置未读回复数
            //user.setUnreadReply(cereRecommendCommentDAO.getUnreadCount(buyerUserId));
            //查询订单数量
            OrderCountDTO dto = cereShopOrderDAO.selectOrderCount(buyerUserId);
            if (dto != null) {
                user.setWaitPayOrderCount(dto.getWaitPayOrderCount());
                user.setWaitSendOrderCount(dto.getWaitSendOrderCount());
                user.setWaitReceiveOrderCount(dto.getWaitReceiveOrderCount());
                user.setTotalOrderCount(dto.getTotalOrderCount());
            }
            //收藏数量
            user.setCollectCount(cereBuyerCollectService.findAllCollectCount(buyerUserId));
            user.setCommentCount(cereShopCommentService.findCommentCount(buyerUserId));
            int problemCount = cereProductProblemService.findProblemCount(buyerUserId);
            int answerCount = cereProductAnswerService.findAnswerCount(buyerUserId);
            user.setAskAnswerCount(problemCount + answerCount);
            user.setVisitCount(cereBuyerFootprintService.findFootprintCount(buyerUserId));
            user.setAfterCount(cereOrderAfterService.findAfterCount(buyerUserId));
            // 设置会员信息
            MemberUtil.setMemberInfo(user);
        }
        return user;
    }

    @Override
    public CerePlatformCanvas getCanvas(CerePlatformCanvas canvas) {
        return cereBuyerUserDAO.getCanvas(canvas);
    }

    @Override
    public Page getSelfProblems(PageParam param,Long buyerUserId) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        //查询当前登录人的所有提问和对应回答数据
        List<SeckillProductProblem> list=cereProductProblemService.getSelfProblems(buyerUserId);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(problem -> {
                //设置回答明细数据
                problem.setAnswers(cereProductAnswerService.findAnswersById(problem.getProblemId()));
            });
        }
        PageInfo<SeckillProductProblem> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public Page getSelfAnswers(PageParam param,Long buyerUserId) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        //查询当前登录人的所有回答过的提问数据
        List<SeckillProductProblem> list=cereProductProblemService.getSelfAnswers(buyerUserId);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(problem -> {
                //设置回答明细数据
                List<SeckillProductAnswer> answers = cereProductAnswerService.findAnswersById(problem.getProblemId());
                if(!EmptyUtils.isEmpty(answers)){
                    //过滤不是当前登录人的回答数据
                    answers=answers.stream().filter(answer -> buyerUserId.equals(answer.getBuyerUserId()))
                            .collect(Collectors.toList());
                }
                problem.setAnswers(answers);
            });
        }
        PageInfo<SeckillProductProblem> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void updatePhone(UserParam param, CereBuyerUser user) throws CoBusinessException {
        BuyerUser byPhone = cereBuyerUserDAO.findByPhone(param.getPhone());
        if(byPhone!=null){
            throw new CoBusinessException(CoReturnFormat.SHOP_PHONE_ALREADY);
        }
        CereBuyerUser cereBuyerUser=new CereBuyerUser();
        cereBuyerUser.setBuyerUserId(user.getBuyerUserId());
        cereBuyerUser.setPhone(param.getPhone());
        cereBuyerUserDAO.updateByPrimaryKeySelective(cereBuyerUser);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void relievePhone(UserParam param, CereBuyerUser user) throws CoBusinessException {
        CereBuyerUser cereBuyerUser=new CereBuyerUser();
        cereBuyerUser.setBuyerUserId(user.getBuyerUserId());
        cereBuyerUserDAO.relievePhone(cereBuyerUser);
    }

    /**
     * 设置token信息
     * @param user
     */
    private void assembleToken(BuyerUser user) {
        //存储token到redis
        TokenInfoBo tokenInfoBo = userRedisService.createToken(user.getPhone(), user.getBuyerUserId());
        user.setToken(tokenInfoBo.getToken());
        user.setRefreshToken(tokenInfoBo.getRefreshToken());
    }
}
