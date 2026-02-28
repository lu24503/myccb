/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.stock.impl;

import com.shop.cereshop.app.dao.activity.CereSignProductDAO;
import com.shop.cereshop.app.dao.platformtool.CerePlatformDiscountDAO;
import com.shop.cereshop.app.dao.platformtool.CerePlatformSeckillDAO;
import com.shop.cereshop.app.page.product.ProductStockInfo;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.discount.CereShopDiscountDetailService;
import com.shop.cereshop.app.service.order.CereOrderProductService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.seckill.CereShopSeckillDetailService;
import com.shop.cereshop.app.service.stock.CereStockService;
import com.shop.cereshop.commons.cache.constants.CacheKeyConstants;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.order.CereOrderProduct;
import com.shop.cereshop.commons.domain.tool.CereShopDiscountDetail;
import com.shop.cereshop.commons.domain.tool.CereShopSeckillDetail;
import com.shop.cereshop.commons.exception.CoBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CereStockServiceImpl implements CereStockService {

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CerePlatformSeckillDAO cerePlatformSeckillDAO;

    @Autowired
    private CerePlatformDiscountDAO cerePlatformDiscountDAO;

    @Autowired
    private CereShopSeckillDetailService cereShopSeckillDetailService;

    @Autowired
    private CereShopDiscountDetailService cereShopDiscountDetailService;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Autowired
    private CereSignProductDAO cereSignProductDAO;

    @Autowired
    private CereOrderProductService cereOrderProductService;

    @Override
    public ProductStockInfo getActivityProductStock(Integer activityType, Long productId) {
        String activityProductStockKey = CacheKeyConstants.ACTIVITY_PRODUCT_STOCK_PREFIX + activityType + "_" + productId;
        String activityProductTotalStockKey = CacheKeyConstants.ACTIVITY_PRODUCT_TOTAL_STOCK_PREFIX + activityType + "_" + productId;
        Integer stock = (Integer)stringRedisService.get(activityProductStockKey);
        Integer totalStock = (Integer)stringRedisService.get(activityProductTotalStockKey);
        if (stock != null && totalStock != null) {
            ProductStockInfo productStockInfo = new ProductStockInfo();
            productStockInfo.setProductId(productId);
            productStockInfo.setStockNumber(stock);
            productStockInfo.setTotal(totalStock);
            return productStockInfo;
        }
        return null;
    }

    @Override
    public ProductStockInfo getActivitySkuStock(Integer activityType, Long skuId) {
        String activitySkuStockKey = CacheKeyConstants.ACTIVITY_SKU_STOCK_PREFIX + activityType + "_" + skuId;
        String activitySkuTotalStockKey = CacheKeyConstants.ACTIVITY_SKU_TOTAL_STOCK_PREFIX + activityType + "_" + skuId;
        return getSkuStockInner(activitySkuStockKey, activitySkuTotalStockKey);
    }

    @Override
    public ProductStockInfo getSkuStockInfo(Long skuId) {
        String skuStockKey = CacheKeyConstants.SKU_STOCK_PREFIX + skuId;
        String skuTotalStockKey = CacheKeyConstants.SKU_TOTAL_STOCK_PREFIX + skuId;
        return getSkuStockInner(skuStockKey, skuTotalStockKey);
    }

    private ProductStockInfo getSkuStockInner(String skuStockKey, String skuTotalStockKey) {
        Integer skuStock = (Integer)stringRedisService.get(skuStockKey);
        Integer skuTotalStock = (Integer)stringRedisService.get(skuTotalStockKey);
        if (skuStock != null && skuTotalStock != null) {
            ProductStockInfo productStockInfo = new ProductStockInfo();
            productStockInfo.setStockNumber(skuStock);
            productStockInfo.setTotal(skuTotalStock);
            return productStockInfo;
        }
        return null;
    }

    @Override
    public void updateActivityProductStock(Integer activityType, Long productId, Integer total, Integer stockNumber) {
        String activityProductStockKey = CacheKeyConstants.ACTIVITY_PRODUCT_STOCK_PREFIX + activityType + "_" + productId;
        String activityProductTotalStockKey = CacheKeyConstants.ACTIVITY_PRODUCT_TOTAL_STOCK_PREFIX + activityType + "_" + productId;
        stringRedisService.set(activityProductStockKey, stockNumber);
        stringRedisService.set(activityProductTotalStockKey, total);
    }

    @Override
    public void updateActivitySkuStock(Integer activityType, Long skuId, Integer total, Integer stockNumber) {
        String activitySkuStockKey = CacheKeyConstants.ACTIVITY_SKU_STOCK_PREFIX + activityType + "_" + skuId;
        String activitySkuTotalStockKey = CacheKeyConstants.ACTIVITY_SKU_TOTAL_STOCK_PREFIX + activityType + "_" + skuId;
        stringRedisService.set(activitySkuStockKey, stockNumber);
        stringRedisService.set(activitySkuTotalStockKey, total);
    }

    @Override
    public void updateSkuStock(Long skuId, Integer total, Integer stockNumber) {
        String skuStockKey = CacheKeyConstants.SKU_STOCK_PREFIX + skuId;
        String skuTotalStockKey = CacheKeyConstants.SKU_TOTAL_STOCK_PREFIX + skuId;
        stringRedisService.set(skuStockKey, stockNumber);
        stringRedisService.set(skuTotalStockKey, total);
    }

    @Override
    public void reduceStock(Integer activityType, Long activityId, Long productId, Long skuId, Integer buyNumber, Integer ifOversold) throws CoBusinessException {
        String lockKey = "";
        RLock lock = null;
        boolean oversoldBool = IntegerEnum.YES.getCode().equals(ifOversold);
        try {
            // 1、校验活动库存
            String activityCacheKey = null;
            int remainStock = 0;
            lockKey = CacheKeyConstants.PRODUCT_STOCK_LOCK_KEY + productId;
            if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(activityType) ||
                    IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(activityType)) {
                activityCacheKey = CacheKeyConstants.ACTIVITY_PRODUCT_STOCK_PREFIX + productId;
            } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(activityType) ||
                    IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(activityType)) {
                activityCacheKey = CacheKeyConstants.ACTIVITY_SKU_STOCK_PREFIX + skuId;
            }
            lock = redissonClient.getLock(lockKey);
            lock.lock();
            if (activityCacheKey != null) {
                Integer stock = (Integer)stringRedisService.get(activityCacheKey);
                if (stock != null) {
                    remainStock = stock;
                    if (remainStock < buyNumber && !oversoldBool) {
                        throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                    } else {
                        remainStock -= buyNumber;
                    }
                } else {
                    //根据不同活动类型查询数据库的库存
                    ProductStockInfo stockInfo = null;
                    if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(activityType)) {
                        stockInfo = cerePlatformSeckillDAO.selectProductStockInfo(activityId, productId);
                    } else if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(activityType)) {
                        stockInfo = cerePlatformDiscountDAO.selectProductStockInfo(activityId, productId);
                    } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(activityType)) {
                        stockInfo = cereShopSeckillDetailService.selectSkuStockInfo(activityId, skuId);
                    } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(activityType)) {
                        stockInfo = cereShopDiscountDetailService.selectSkuStockInfo(activityId, skuId);
                    }
                    if (stockInfo != null && stockInfo.getStockNumber() != null) {
                        if (stockInfo.getStockNumber() < buyNumber && !oversoldBool) {
                            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                        }
                        remainStock = stockInfo.getStockNumber() - buyNumber;
                    } else {
                        Integer stockNumber = cereProductSkuService.findStockNumber(skuId);
                        if (stockNumber < buyNumber && !oversoldBool) {
                            throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                        }
                        remainStock = stockNumber - buyNumber;
                    }
                }
            }

            // 2、校验常规sku库存
            String skuCacheKey = CacheKeyConstants.SKU_STOCK_PREFIX + skuId;
            Integer skuStock = (Integer)stringRedisService.get(skuCacheKey);
            int skuRemainStock = 0;
            if (skuStock != null) {
                if (skuStock < buyNumber && !oversoldBool) {
                    throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                }
                skuRemainStock = skuStock - buyNumber;
            } else {
                Integer stockNumber = cereProductSkuService.findStockNumber(skuId);
                if (stockNumber < buyNumber && !oversoldBool) {
                    throw new CoBusinessException(CoReturnFormat.PRODUCT_STOCK_ERROR);
                }
                skuRemainStock = stockNumber - buyNumber;
            }

            // 3、更新活动库存量
            if (activityCacheKey != null) {
                stringRedisService.set(activityCacheKey, remainStock);
                //更新活动数据库中的库存量
                if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(activityType)) {
                    cereSignProductDAO.updateNumber(IntegerEnum.ACTIVITY_SECKILL.getCode(), activityId, productId, remainStock);
                } else if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(activityType)) {
                    cereSignProductDAO.updateNumber(IntegerEnum.ACTIVITY_DISCOUNT.getCode(), activityId, productId, remainStock);
                } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(activityType)) {
                    CereShopSeckillDetail seckillDetail = new CereShopSeckillDetail();
                    seckillDetail.setShopSeckillId(activityId);
                    seckillDetail.setNumber(remainStock);
                    seckillDetail.setSkuId(skuId);
                    cereShopSeckillDetailService.updateNumber(seckillDetail);
                } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(activityType)) {
                    CereShopDiscountDetail discountDetail = new CereShopDiscountDetail();
                    discountDetail.setShopDiscountId(activityId);
                    discountDetail.setNumber(remainStock);
                    discountDetail.setSkuId(skuId);
                    cereShopDiscountDetailService.updateNumber(discountDetail);
                }
            }

            // 4、更新常规sku库存量
            stringRedisService.set(skuCacheKey, skuRemainStock);
            cereProductSkuService.updateStockNumber(skuId, skuRemainStock);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    @Override
    public void rollbackStock(Integer activityType, Long activityId, Long productId, Long skuId, Integer buyNumber) {
        String lockKey = "";
        RLock lock = null;
        try {
            // 1、校验活动库存
            String activityCacheKey = null;
            lockKey = CacheKeyConstants.PRODUCT_STOCK_LOCK_KEY + productId;
            if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(activityType) ||
                    IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(activityType)) {
                activityCacheKey = CacheKeyConstants.ACTIVITY_PRODUCT_STOCK_PREFIX + productId;
            } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(activityType) ||
                    IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(activityType)) {
                activityCacheKey = CacheKeyConstants.ACTIVITY_SKU_STOCK_PREFIX + skuId;
            }
            lock = redissonClient.getLock(lockKey);
            lock.lock();
            if (activityCacheKey != null) {
                boolean exists = stringRedisService.isExist(activityCacheKey);
                if (exists) {
                    stringRedisService.incr(activityCacheKey, buyNumber);
                }
                // 有可能 活动已结束，缓存库存删除了，但是数据库的库存还在，这时候也有必要库存回流
                if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_SECKILL.getCode().equals(activityType)) {
                    cerePlatformSeckillDAO.rollbackStock(activityId, productId, buyNumber);
                } else if (IntegerEnum.ACTIVITY_TYPE_PLATFORM_DISCOUNT.getCode().equals(activityType)) {
                    cerePlatformDiscountDAO.rollbackStock(activityId, productId, buyNumber);
                } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(activityType)) {
                    cereShopSeckillDetailService.rollbackStock(activityId, skuId, buyNumber);
                } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(activityType)) {
                    cereShopDiscountDetailService.rollbackStock(activityId, skuId, buyNumber);
                }
            }

            // 2、校验常规sku库存
            String skuCacheKey = CacheKeyConstants.SKU_STOCK_PREFIX + skuId;
            boolean exists = stringRedisService.isExist(skuCacheKey);
            if (exists) {
                stringRedisService.incr(skuCacheKey, buyNumber);
            }
            cereProductSkuService.rollbackStock(skuId, buyNumber);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    @Override
    public void rollbackStockByOrderId(Long orderId) {
        //拿到商品库存数据
        List<CereOrderProduct> opList = cereOrderProductService.findByOrderIds(Collections.singletonList(orderId));
        for (CereOrderProduct op:opList) {
            rollbackStock(op.getActivityType(), op.getActivityId(), op.getProductId(), op.getSkuId(), op.getNumber());
        }
    }

    @Override
    public ProductStockInfo getOrQueryShopActivitySkuStock(Integer activityType, Long activityId, Long skuId) {
        ProductStockInfo stockInfo = getActivitySkuStock(activityType, skuId);
        if (stockInfo != null) {
            return stockInfo;
        }
        if (IntegerEnum.ACTIVITY_TYPE_SHOP_SECKILL.getCode().equals(activityType)) {
            stockInfo = cereShopDiscountDetailService.selectSkuStockInfo(activityId, skuId);
        } else if (IntegerEnum.ACTIVITY_TYPE_SHOP_DISCOUNT.getCode().equals(activityType)) {
            stockInfo = cereShopSeckillDetailService.selectSkuStockInfo(activityId, skuId);
        }
        if (stockInfo != null) {
            updateActivitySkuStock(activityType, skuId, stockInfo.getTotal(), stockInfo.getStockNumber());
        }
        return stockInfo;
    }
}
