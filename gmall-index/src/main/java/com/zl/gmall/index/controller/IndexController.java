package com.zl.gmall.index.controller;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.index.service.IndexService;
import com.zl.gmall.pms.entity.CategoryEntity;
import com.zl.gmall.pms.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-01 11:57
 */
@RestController
@RequestMapping("/index") //http://localhost:2000/api/index/cates
public class IndexController {

    @Autowired
    private IndexService indexService;
    @GetMapping("/cates")
    public Resp<List<CategoryEntity>> queryCategoryByPid(){
        List<CategoryEntity> categoryEntityList=indexService.queryCategoryByPid();
        return Resp.ok(categoryEntityList);
    }

    // http://localhost:2000/api/index/cates/2
    //根据ID查询二级和三级分类
    @GetMapping("/cates/{pid}")
    public Resp<List<CategoryVo>> queryCategoryLevelByPid(@PathVariable("pid")Long pid){
        List<CategoryVo> categoryVoList=indexService.queryCategoryLevelByPid(pid);
        System.out.println(categoryVoList);
        return Resp.ok(categoryVoList);
    }
    /*
        无法使用本地锁来解决并发的问题：
        本地锁是一个单线程的，分布式是多进程多线程的，由于进程和进程之间不存在线程的
        共享的问题，所以JVM锁是无法解决分布式的问题
     */

    @GetMapping("test/lock")
    public Resp<Object> test(){
        indexService.testLock();
        return Resp.ok(null);

    }




}
