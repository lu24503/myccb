/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.shop.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luciad.imageio.webp.WebPReadParam;
import com.shop.cereshop.app.dao.shop.CerePlatformShopDAO;
import com.shop.cereshop.app.page.banner.ShopBanner;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.index.RecommendShop;
import com.shop.cereshop.app.page.product.ProductCoupon;
import com.shop.cereshop.app.page.settlement.SettlementShop;
import com.shop.cereshop.app.page.shop.*;
import com.shop.cereshop.app.param.shop.ShopParam;
import com.shop.cereshop.app.param.shop.ShopPosterParam;
import com.shop.cereshop.app.service.buyer.CereBuyerShopCouponService;
import com.shop.cereshop.app.service.collect.CereBuyerCollectService;
import com.shop.cereshop.app.service.distributor.CereShopDistributorService;
import com.shop.cereshop.app.service.product.CereShopProductService;
import com.shop.cereshop.app.service.shop.*;
import com.shop.cereshop.app.utils.AlipayUtil;
import com.shop.cereshop.app.utils.WechatUtil;
import com.shop.cereshop.commons.cache.product.ProductBo;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.collect.CereBuyerCollect;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.shop.CerePlatformShop;
import com.shop.cereshop.commons.domain.shop.CereShopReturn;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.upload.strategy.FileStrategy;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.QRCodeUtil;
import com.shop.cereshop.commons.utils.TimeUtils;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CerePlatformShopserviceImpl implements CerePlatformShopservice {

    @Autowired
    private CerePlatformShopDAO cerePlatformShopDAO;

    @Autowired
    private CereShopReturnService cereShopReturnService;

    @Autowired
    private CereShopOtherOrganizationsService cereShopOtherOrganizationsService;

    @Autowired
    private CereShopEnterpriseService cereShopEnterpriseService;

    @Autowired
    private CereShopPersonalService cereShopPersonalService;

    @Autowired
    private CereShopIndividualBusinessesService cereShopIndividualBusinessesService;

    @Autowired
    private CereBuyerCollectService cereBuyerCollectService;

    @Autowired
    private CereBuyerShopCouponService cereBuyerShopCouponService;

    @Autowired
    private CereShopProductService cereShopProductService;

    @Autowired
    private CereShopDistributorService cereShopDistributorService;

    @Autowired
    private FileStrategy fileStrategy;

    /**
     * 店铺页面前缀
     */
    @Value("${shop_url}")
    private String shopUrl;

    /**
     * 店铺招募页面前缀
     */
    @Value("${recruit_url}")
    private String recruitUrl;

    @Override
    public List<RecommendShop> findRecommendShop() {
        return cerePlatformShopDAO.findRecommendShop();
    }

    @Override
    public RecommendShop findVolumeProductByShopId(Long shopId) {
        return cerePlatformShopDAO.findVolumeProductByShopId(shopId);
    }

    @Override
    public List<RecommendShop> findRecommendProducts() {
        return cerePlatformShopDAO.findRecommendProducts();
    }

    @Override
    public Integer findPayUsers(Long productId) {
        Integer payUsers = cerePlatformShopDAO.findPayUsers(productId);
        int fictitiousNumber = cereShopProductService.selectFictitiousNumber(productId);
        return payUsers + fictitiousNumber;
    }

    @Override
    public Shop getShopProducts(ShopParam param, CereBuyerUser user) throws CoBusinessException {
        //查询店铺已售件数
        Shop shop=cerePlatformShopDAO.findNumber(param.getShopId());
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<Product> list=cerePlatformShopDAO.getShopProducts(param);
        if(!EmptyUtils.isEmpty(list)){
            List<Long> skuIdList = list.stream().map(Product::getSkuId).collect(Collectors.toList());
            Map<Long, Product> productMap = cereShopProductService.getSimpleActivityInfo(param.getShopId(), skuIdList, user);
            for (Product product:list) {
                Product activityProduct = productMap.get(product.getSkuId());
                if (activityProduct != null) {
                    product.setActivityType(activityProduct.getActivityType());
                    product.setOriginalPrice(activityProduct.getOriginalPrice());
                    product.setPrice(activityProduct.getPrice());
                    product.setUsers(activityProduct.getUsers());
                }
            }
        }
        PageInfo<Product> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        shop.setPage(page);
        return shop;
    }

    @Override
    public List<ShopClassify> getShopClassify(ShopParam param) throws CoBusinessException {
        return cerePlatformShopDAO.getShopClassify(param.getShopId());
    }

    @Override
    public Page getExtensionProduct(ShopParam param) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<Product> list=cerePlatformShopDAO.getExtensionProduct(param);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(a -> {
                //查询付款人数
                a.setUsers(findPayUsers(a.getProductId()));
                //设置活动信息
                ProductBo productBo = cereShopProductService.fetchProductCache(a.getShopId(), a.getProductId(), a.getSkuId(), null, false);
                if (productBo != null) {
                    a.setActivityType(productBo.getActivityType());
                    a.setOriginalPrice(productBo.getOriginalPrice());
                    a.setPrice(productBo.getPrice());
                    a.setTotal(productBo.getTotal());
                }
            });
        }
        PageInfo<Product> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public List<ProductCoupon> findShopToolByProductId(Long shopId, Long productId) {
        return cerePlatformShopDAO.findShopToolByProductId(shopId,productId);
    }

    @Override
    public List<ShopBanner> getShopBanner(Long shopId) throws CoBusinessException {
        return cerePlatformShopDAO.getShopBanner(shopId);
    }

    @Override
    public Page getShops(ShopParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<SettlementShop> list=cerePlatformShopDAO.getShops(param);
        if(!EmptyUtils.isEmpty(list)){
            //查询满足条件商品数据
            list.forEach(shop -> {
                param.setShopId(shop.getShopId());
                List<CartSku> skus = cerePlatformShopDAO.findSkuByShopParam(param);
                if (CollectionUtils.isNotEmpty(skus)) {
                    List<Long> skuIdList = skus.stream().map(CartSku::getSkuId).collect(Collectors.toList());
                    Map<Long, Product> activityMap = cereShopProductService.getSimpleActivityInfo(shop.getShopId(), skuIdList, user);
                    for (CartSku sku:skus) {
                        if (activityMap.get(sku.getSkuId()) != null) {
                            sku.setActivityType(activityMap.get(sku.getSkuId()).getActivityType());
                            sku.setPrice(activityMap.get(sku.getSkuId()).getPrice());
                        }
                    }
                }
                shop.setSkus(skus);
            });
        }
        PageInfo<SettlementShop> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public void updateState(CerePlatformShop cerePlatformShop) throws CoBusinessException {
        cerePlatformShopDAO.updateState(cerePlatformShop);
    }

    @Override
    public void insert(CerePlatformShop cerePlatformShop) throws CoBusinessException {
        cerePlatformShopDAO.insert(cerePlatformShop);
    }

    @Override
    public void update(CerePlatformShop cerePlatformShop) throws CoBusinessException {
        cerePlatformShopDAO.updateByPrimaryKeySelective(cerePlatformShop);
    }

    @Override
    public PlatformShop getById(Long shopId) throws CoBusinessException {
        //查询店铺信息
        PlatformShop shop=cerePlatformShopDAO.findShop(shopId);
        if(shop!=null){
            //查询店铺退货地址信息
            CereShopReturn cereShopReturn=cereShopReturnService.findByShopId(shopId);
            shop.setShopReturn(cereShopReturn);
            //查询认证数据
            if(IntegerEnum.getByName("主体类型-个人").equals(shop.getAuthenType())){
                //个人认证
                shop.setPersonal(cereShopPersonalService.findByShopId(shopId));
            }else if(IntegerEnum.getByName("主体类型-个体工商户").equals(shop.getAuthenType())){
                //个体工商户户认证
                shop.setIndividualBusinesses(cereShopIndividualBusinessesService.findByShopId(shopId));
            }else if(IntegerEnum.getByName("主体类型-企业").equals(shop.getAuthenType())){
                //企业认证
                shop.setEnterprise(cereShopEnterpriseService.findByShopId(shopId));
            }else if(IntegerEnum.getByName("主体类型-其他组织").equals(shop.getAuthenType())){
                //其他组织认证
                shop.setOtherOrganizations(cereShopOtherOrganizationsService.findByShopId(shopId));
            }
        }
        return shop;
    }

    @Override
    public CerePlatformShop findByShopName(String shopName) {
        return cerePlatformShopDAO.findByShopName(shopName);
    }

    @Override
    public CerePlatformShop findByPhone(String shopPhone) {
        return cerePlatformShopDAO.findByPhone(shopPhone);
    }

    @Override
    public CerePlatformShop checkShopIdByName(String shopName, Long shopId) {
        return cerePlatformShopDAO.checkShopIdByName(shopName,shopId);
    }

    @Override
    public CerePlatformShop checkShopIdByPhone(String shopPhone, Long shopId) {
        return cerePlatformShopDAO.checkShopIdByPhone(shopPhone,shopId);
    }

    @Override
    public CerePlatformShop check(String shopPhone) {
        return cerePlatformShopDAO.check(shopPhone);
    }

    @Override
    public ShopIndex getIndex(ShopParam param,CereBuyerUser user) throws CoBusinessException,Exception {
        String time =TimeUtils.yyMMddHHmmss();
        //查询店铺数据
        ShopIndex index=cerePlatformShopDAO.findShopIndex(param.getShopId());
        if(index!=null){
            //查询店铺粉丝数
            index.setFansNumber(cereBuyerCollectService.findShopCollectCount(index.getShopId()));
            //查询店铺商品总类数
            index.setClassifyNumber(cereShopProductService.findClassifyNumber(param.getShopId()).size());
            //查询店铺已售件数
            index.setNumber(cerePlatformShopDAO.findShopNumber(param.getShopId()));
            if(user!=null){
                //默认未收藏
                index.setIfCollect(IntegerEnum.NO.getCode());
                //查询收藏id
                CereBuyerCollect cereBuyerCollect = cereBuyerCollectService.findByUserShopId(user.getBuyerUserId(), index.getShopId());
                if(cereBuyerCollect!=null){
                    index.setCollectId(cereBuyerCollect.getCollectId());
                    index.setIfCollect(cereBuyerCollect.getState());
                }
            }
            //查询店铺优惠券数据
            List<ShopCoupon> coupons=cerePlatformShopDAO.findShopCoupons(param.getShopId());
            if(!EmptyUtils.isEmpty(coupons)&&user!=null){
                if(user!=null){
                    //设置优惠券状态
                    for (int i = 0; i < coupons.size(); i++) {
                        if(i<0){
                            i=0;
                        }
                        ShopCoupon shopCoupon=coupons.get(i);
                        //查询用户已领取的优惠券总数
                        int count = cereBuyerShopCouponService.findCount(user.getBuyerUserId(),shopCoupon.getShopCouponId());
                        if(count<=0&&shopCoupon.getStockNumber()<=0){
                            //未领取过且库存数量为0，过滤这个优惠券
                            coupons.remove(i);
                            i--;
                            continue;
                        }
                        if(!EmptyUtils.isEmpty(shopCoupon.getFrequency())){
                            //如果有限制领取次数
                            if(shopCoupon.getFrequency()==count){
                                //如果限制领取次数=用户已领取优惠券总数,修改店铺优惠券状态为已领取
                                shopCoupon.setState(IntegerEnum.COUPON_HAVE.getCode());
                            }
                        }
                    }
                }else {
                    //未登录
                    for (int i = 0; i < coupons.size(); i++) {
                        if(i<0){
                            i=0;
                        }
                        ShopCoupon shopCoupon=coupons.get(i);
                        if(shopCoupon.getStockNumber()<=0){
                            //如果库存小于等于0,过滤优惠券
                            coupons.remove(i);
                            i--;
                        }
                    }
                }
            }
            index.setCoupons(coupons);
            //查询拼团专区数据
            List<ShopGroupWork> shopGroupWork=cerePlatformShopDAO.findGroupWork(param.getShopId());
            if(!EmptyUtils.isEmpty(shopGroupWork)){
                shopGroupWork.forEach(work -> {
                    //查询拼团专区商品数据
                    work.setGroupProducts(cerePlatformShopDAO.findGroupProducts(work.getShopGroupWorkId()));
                });
            }
            index.setShopGroupWork(shopGroupWork);
            //查询秒杀专区数据
            List<ShopSeckill> shopSeckill=cerePlatformShopDAO.findSeckill(param.getShopId());
            if(!EmptyUtils.isEmpty(shopSeckill)){
                for (ShopSeckill seckill : shopSeckill) {
                    //查询秒杀专区商品数据
                    seckill.setSeckillProducts(cerePlatformShopDAO.findSeckillProducts(seckill.getShopSeckillId()));
                    //设置活动倒计时时间
                    seckill.setTime(TimeUtils.getCountDownByTime(time,seckill.getEffectiveEnd()));
                }
            }
            index.setShopSeckill(shopSeckill);
            //查询限时折扣专区数据
            List<ShopDiscount> shopDiscount=cerePlatformShopDAO.findDiscount(param.getShopId());
            if(!EmptyUtils.isEmpty(shopDiscount)){
                for (ShopDiscount discount : shopDiscount) {
                    //查询限时折扣专区商品数据
                    discount.setDiscountProducts(cerePlatformShopDAO.findDiscountProducts(discount.getShopDiscountId()));
                    //设置活动倒计时时间
                    discount.setTime(TimeUtils.getCountDownByTime(time,discount.getEndTime()));
                }
            }
            index.setShopDiscount(shopDiscount);
        }
        return index;
    }

    @Override
    public String getSharePic(ShopPosterParam param, CereBuyerUser user) throws CoBusinessException{
        if (user == null) {
            throw new CoBusinessException(CoReturnFormat.USER_NOT_LOGIN);
        }
        try {
            PlatformShop shop = getById(param.getShopId());
            int env = param.getTerminal();

            ShopParam shopParam = new ShopParam();
            shopParam.setShopId(param.getShopId());
            PageHelper.startPage(1, 8);
            List<Product> list = cerePlatformShopDAO.getShopProducts(shopParam);
            if(!EmptyUtils.isEmpty(list)) {
                list.forEach(a -> {
                    //查询付款人数
                    a.setUsers(findPayUsers(a.getProductId()));
                    //设置活动相关信息
                    ProductBo productBo = cereShopProductService.fetchProductCache(a.getShopId(), a.getProductId(), a.getSkuId(), user, false);
                    if (productBo != null) {
                        a.setActivityType(productBo.getActivityType());
                        a.setOriginalPrice(productBo.getOriginalPrice());
                        a.setPrice(productBo.getPrice());
                        a.setTotal(productBo.getTotal());
                    }
                });
            }

            String shareHeadUrl = user.getHeadImage();
            String shareName = user.getName();
            if (StringUtils.isBlank(shareName)) {
                shareName = user.getWechatName();
            }
            String invitationCode = null;
            if (param.getDistributorId() != null) {
                invitationCode = cereShopDistributorService.findInvitationCode(param.getDistributorId());
            }

            String accessToken = WechatUtil.getAccessToken();
            return genPoster(env,param.getType(),shop.getShopId(),shareHeadUrl,shareName,shop.getShopBrief(),invitationCode,list,accessToken,param.getDistributorId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成分享海报
     * @param env 1-APP 2-微信小程序 3-H5 4-支付宝小程序
     * @param shopId
     * @param shareHeadUrl
     * @param shareName
     * @param shopBrief
     * @param invitationCode
     * @param productList
     * @param accessToken
     * @param distributorId 分销员id
     */
    private String genPoster(int env, Integer type, Long shopId, String shareHeadUrl, String shareName,
                             String shopBrief, String invitationCode, List<Product> productList,
                             String accessToken, Long distributorId) throws Exception {
        int width = 640;
        int height = 1220;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = image.createGraphics();

        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        g2d.dispose();
        g2d = image.createGraphics();

        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, width, height);

        g2d.setColor(new Color(182,249,225));
        g2d.setStroke(new BasicStroke(1));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        BufferedImage shareHeadImg = ImageIO.read(new URL(shareHeadUrl));

        //把图片切成一个圓
        {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
            g2d.setStroke(s);
            //留一个像素的空白区域，这个很重要，画圆的时候把这个覆盖
            int border = 1;
            //图片是一个圆型
            Ellipse2D.Double shape = new Ellipse2D.Double(20, 40, 120 - border * 2, 120 - border * 2);
            //需要保留的区域
            g2d.setClip(shape);
            g2d.drawImage(shareHeadImg.getScaledInstance(118, 118, Image.SCALE_SMOOTH), 20, 40, 120 - border * 2, 120 - border * 2, null);
            g2d.dispose();
        }

        g2d = image.createGraphics();
        g2d.setStroke(new BasicStroke(1));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //分享人名称

        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("Alibaba PuHuiTi R", Font.PLAIN, 21));
        g2d.drawString(shareName, 170, 92);

        //店铺简介
        if (shopBrief != null) {
            g2d.setPaint(new Color(194, 194, 194));
            g2d.setFont(new Font("Alibaba PuHuiTi R", Font.PLAIN, 24));
            FontMetrics fm2 = g2d.getFontMetrics(g2d.getFont());
            while (fm2.stringWidth(shopBrief) > 450) {
                shopBrief = shopBrief.substring(0, shopBrief.length() - 1);
            }
            g2d.drawString(shopBrief, 170, 150);
        }

        //邀请码 邀请下级的时候才需要展示
        if (type != null && type == 2) {
            g2d.setPaint(new Color(249, 220, 116));
            g2d.setFont(new Font("Alibaba PuHuiTi R", Font.PLAIN, 24));
            g2d.drawString("邀请码: " + invitationCode, 375, 95);
        }

        //商品列表背景
        g2d.setPaint(new Color(244, 245, 247));
        g2d.setBackground(new Color(244, 245, 247));
        g2d.clearRect(16, 186, width - 16 * 2, 790);

        //商品列表
        if (!CollectionUtils.isEmpty(productList)) {
            int count = 0;
            int productHeight = 368;
            g2d.setBackground(Color.WHITE);
            for (Product product:productList) {
                int marginLeft = 338;
                if (count % 2 == 0) {
                    marginLeft = 71;
                }
                int marginTop = count / 2 * (productHeight + 16) + 206;
                int productImgWidth = 231;
                int productImgHeight = 231;
                g2d.clearRect(marginLeft, marginTop, productImgWidth, productHeight);

                BufferedImage proImg = readImgInner(product.getImage());
                if (proImg == null) {
                    continue;
                }
                g2d.drawImage(proImg.getScaledInstance(productImgWidth, productImgHeight, Image.SCALE_SMOOTH), marginLeft, marginTop, null);

                //间距20

                //商品名称
                FontMetrics fmPro = g2d.getFontMetrics(g2d.getFont());
                g2d.setPaint(Color.BLACK);
                g2d.setFont(new Font("Alibaba PuHuiTi R", Font.PLAIN, 18));
                String productName = product.getProductName();
                while(fmPro.stringWidth(productName) > 150 && productName.length() > 1) {
                    productName = productName.substring(0, productName.length() - 1);
                }
                //文字位置是从底部算起的，所以加上间距 还要加上字体本身
                g2d.drawString(productName, marginLeft + 12, marginTop + productImgHeight + 20 + 18);

                //间距16

                //多少人付款
                g2d.setPaint(new Color(236, 179, 30));
                g2d.setFont(new Font("Alibaba PuHuiTi R", Font.PLAIN, 15));
                g2d.drawString(product.getUsers() + "人付款", marginLeft + 12, marginTop + productImgHeight + 20 + 18 + 16 + 15);

                //间距22

                //售价 和 原价
                g2d.setPaint(new Color(225, 37, 27));
                g2d.setFont(new Font("Alibaba PuHuiTi R", Font.PLAIN, 16));
                g2d.drawString("￥" + product.getPrice(), marginLeft + 12, marginTop + productImgHeight + 20 + 18 + 16 + 15 + 22 + 16);

                FontMetrics priceFm = g2d.getFontMetrics(g2d.getFont());
                int priceWidth = priceFm.stringWidth("￥" + product.getPrice());

                g2d.setPaint(Color.GRAY);
                g2d.setFont(new Font("Alibaba PuHuiTi R", Font.PLAIN, 13));
                FontMetrics originalPriceFm = g2d.getFontMetrics(g2d.getFont());
                int originalPriceWidth = originalPriceFm.stringWidth("￥" + product.getOriginalPrice());
                int originalPriceMarginLeft = marginLeft + 12 + priceWidth + 24;
                int originalPriceMarginTop = marginTop + productImgHeight + 20 + 18 + 16 + 15 + (22 + 4) + 13;
                g2d.drawString("￥" + product.getOriginalPrice(), originalPriceMarginLeft, originalPriceMarginTop);
                //画删除线
                int delLineMarginTop = originalPriceMarginTop - g2d.getFont().getSize() / 2;
                g2d.drawLine(originalPriceMarginLeft - 2, delLineMarginTop, originalPriceMarginLeft + originalPriceWidth + 4, delLineMarginTop);
                count++;
                if (count == 4) {
                    break;
                }
            }
        }

        //生成底部文案 和 二维码
        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("Alibaba PuHuiTi R", Font.BOLD, 17));
        BufferedImage qrCode = null;
        if (env == 1 || env == 2) {
            g2d.drawString("长按识别小程序", 30, 1036);
            String url = "pages_category_page1/store/index?storeId=" + shopId + "&salesId=" + distributorId;
            if (type != null && type == 2) {
                url = "pages_category_page1/distributionModule/recruit?shopId=" + shopId + "&invitationCode=" + invitationCode;
            }
            qrCode = WechatUtil.getWxacode(accessToken, url);
        } else if (env == 3) {
            g2d.drawString("扫描二维码购买商品", 30, 1036);
            String url = shopUrl + "storeId=" + shopId + "&salesId=" + distributorId;
            if (type != null && type == 2) {
                url = recruitUrl + "shopId=" + shopId + "&invitationCode=" + invitationCode;
            }
            qrCode = QRCodeUtil.createImage(url, null, false);
        } else if (env == 4) {
            g2d.drawString("长按识别小程序", 30, 1036);
            String url = "pages_category_page1/store/index";
            String reqParam = "storeId=" + shopId + "&salesId=" + distributorId;
            if (type != null && type == 2) {
                url = "pages_category_page1/distributionModule/recruit";
                reqParam = "shopId=" + shopId + "&invitationCode=" + invitationCode;
            }
            String qrCodeUrl = AlipayUtil.generateQrCode(url, reqParam);
            qrCode = ImageIO.read(new URL(qrCodeUrl));
            qrCode = qrCode.getSubimage(102, 134, 1336, 1336);
        }
        if (qrCode != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.drawImage(qrCode, 390, 976, 240, 240, null);
        }

        g2d.dispose();

        String fileName = UUID.randomUUID().toString() + ".png";
        InputStream inputStream = bufferedImageToInputStream(image);
        ByteArrayOutputStream baos = cloneInputStream(inputStream);
        return fileStrategy.uploadStream(fileName, IOUtils.toByteArray(new ByteArrayInputStream(baos.toByteArray())), new ByteArrayInputStream(baos.toByteArray()), inputStream.available());
    }

    private BufferedImage readImgInner(String imageUrl) throws Exception {
        if (StringUtil.isNotBlank(imageUrl)) {
            if (imageUrl.endsWith(".webp")) {
                // Obtain a WebP ImageReader instance
                ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

                // Configure decoding parameters
                WebPReadParam readParam = new WebPReadParam();
                readParam.setBypassFiltering(true);

                // Configure the input on the ImageReader
                reader.setInput(ImageIO.createImageInputStream(new URL(imageUrl).openStream()));

                // Decode the image
                return reader.read(0, readParam);
            } else {
                return ImageIO.read(new URL(imageUrl));
            }
        }
        return null;
    }

    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将BufferedImage转换为InputStream
     * @param image
     * @return
     */
    public InputStream bufferedImageToInputStream(BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
