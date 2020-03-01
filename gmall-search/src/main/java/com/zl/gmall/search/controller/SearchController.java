package com.zl.gmall.search.controller;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.search.service.SearcherService;
import com.zl.gmall.search.vo.SearchParamVo;
import com.zl.gmall.search.vo.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author shkstart
 * @create 2020-02-28 22:45
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearcherService searcherService;

    @GetMapping
    public Resp<SearchResponseVo> search(SearchParamVo searchParamVo) throws IOException {
        SearchResponseVo searchResponseVo = searcherService.search(searchParamVo);
        return Resp.ok(searchResponseVo);
    }
}
