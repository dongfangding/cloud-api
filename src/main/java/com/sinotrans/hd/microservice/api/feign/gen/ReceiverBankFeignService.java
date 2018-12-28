package com.sinotrans.hd.microservice.api.feign.gen;

import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.GEN_APPLICATION_NAME, path = "/HDGen/receiverBank")
public interface ReceiverBankFeignService {

    /**
     * 返回指定收款人的银行卡信息
     *
     * @param orgCode      公司代码
     * @param receiverName 收款人名称
     * @param type         收款人类型
     * @param currCode     银行卡币种
     * @return
     */
    @RequestMapping("/receiverBank")
    List<Map<String, Object>> getReceiverBank(
            @RequestParam(value = "orgCode") String orgCode,
            @RequestParam(value = "receiverName") String receiverName,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "currCode", required = false) String currCode);
}
