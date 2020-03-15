package com.zl.gmall.wms.service.impl;

import com.alibaba.fastjson.JSON;
import com.zl.gmall.wms.vo.SkuLockVo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.wms.dao.WareSkuDao;
import com.zl.gmall.wms.entity.WareSkuEntity;
import com.zl.gmall.wms.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    private static final String KEY_PRIFIX="wms:lock:";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WareSkuDao wareSkuDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageVo(page);
    }

    /**
     *
     * 库存的业务逻辑分析:
     * 遍历每个商品验库存并且锁库存
     * 当前的库存数-锁定的库存数如果大于当前的需要的库存数显示有货
     * setLocke为false
     * 锁库存:
     * 更新当前库存的锁住的数量
     */
    @Transactional
    @Override
    public List<SkuLockVo> checkAndCheck(List<SkuLockVo> skuLockVos) {
        if(CollectionUtils.isEmpty(skuLockVos)){
            return null;
        }

        skuLockVos.forEach(skuLockVo->{
            //遍历每个商品验库存并锁住库存
            this.checkLock(skuLockVo);
        });

        //如果有一个商品锁定失败了,所有已经成功锁定的商品要解库存
        List<SkuLockVo> successLockVos = skuLockVos.stream().filter(SkuLockVo::getLock).collect(Collectors.toList());
        //已经失败的库存
        List<SkuLockVo> errorLockVo = skuLockVos.stream().filter(skuLockVo ->!skuLockVo.getLock()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(errorLockVo)){ //如果有商品锁定失败
            successLockVos.forEach(lockVo->{
                this.wareSkuDao.unlockStock(lockVo.getWareSkuId(),lockVo.getCount());
            });
            return skuLockVos;
        }

        //把库存信息锁定信息保存到Redis中,以方便解锁库存
        String orderToken = skuLockVos.get(0).getOrderToken();
        this.redisTemplate.opsForValue().set(KEY_PRIFIX+orderToken, JSON.toJSONString(skuLockVos));

        return null;
    }

    //验库存并锁住存

    private void checkLock(SkuLockVo skuLockVo){
        RLock fairLock = this.redissonClient.getFairLock("lock:" + skuLockVo.getSkuId());
        fairLock.lock();
        //验库存,如果有足够的库存
        List<WareSkuEntity> wareSkuEntities = this.wareSkuDao.checkStock(skuLockVo.getSkuId(), skuLockVo.getCount());
        if(CollectionUtils.isEmpty(wareSkuEntities)){
            skuLockVo.setLock(false); //库存不足，锁库失败
            fairLock.unlock();
            return;
        }

        //锁库存+锁住的库存
        if(this.wareSkuDao.lockStock(wareSkuEntities.get(0).getId(), skuLockVo.getCount())==1){
            skuLockVo.setLock(true);
            skuLockVo.setWareSkuId(wareSkuEntities.get(0).getId());

        }else{
            //锁库存失败
            skuLockVo.setLock(false);
        }
        fairLock.unlock();
    }

}