package com.zl.gmall.search.repository;

import com.zl.gmall.search.vo.GoodsVo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author shkstart
 * @create 2020-02-27 12:03
 */
public interface GoodsRepository extends ElasticsearchRepository<GoodsVo,Long> {
}
