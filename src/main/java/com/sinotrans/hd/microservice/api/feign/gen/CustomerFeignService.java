package com.sinotrans.hd.microservice.api.feign.gen;

import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.GEN_APPLICATION_NAME, path = "/HDGen/customer")
public interface CustomerFeignService {

    /**
     * 查询工商信息接口
     * @param keyWord
     * @return
     */
    @RequestMapping(value = {"fetchBuinessInfo"})
    Map<String, Object> fetchBusinessInfo(@RequestParam(value = "keyWord") String keyWord);
}
