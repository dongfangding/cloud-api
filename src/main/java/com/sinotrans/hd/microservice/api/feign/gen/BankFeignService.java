package com.sinotrans.hd.microservice.api.feign.gen;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.GEN_APPLICATION_NAME, path = "/HDGen/bank")
public interface BankFeignService {

    /**
     * 银行列表查询
     * @param bankKey 银行名称代码模糊匹配
     * @param page 当前页，默认1
     * @param lines 每页显示条数默认15
     * @return 银行列表
     */
    @RequestMapping("/banks")
    Page<Object> getBankList(
            @RequestParam(value = "bankKey", required = false) String bankKey,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines);
}
