package com.sinotrans.hd.microservice.api.feign.gen;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 客户与供应商中心收款人相关接口调用
 * @author DDf on 2018/8/2
 */
@FeignClient(value = Constants.GEN_APPLICATION_NAME, path = "/HDGen/receiver")
public interface ReceiverFeignService {

    /**
     * 收款人列表查询
     *
     * @param orgCode        公司代码
     * @param q              查询字段
     * @param type           类型
     * @param filterNames    过滤收款人的全称，多个用,分隔
     * @param isLike         true则查询字段为LIKE ， false则查询字段为like
     * @param page           当前页数
     * @param lines          每页显示条数
     * @return
     */
    @RequestMapping("/receivers")
    Page<Object> getReceiverList(
            @RequestParam(value = "orgCode") String orgCode, @RequestParam(required = false, value = "q") String q,
            @RequestParam(required = false, value = "type") Integer type,
            @RequestParam(required = false, value = "filterNames") String filterNames,
            @RequestParam(value = "isLike", defaultValue = "1", required = false) boolean isLike,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines);
}
