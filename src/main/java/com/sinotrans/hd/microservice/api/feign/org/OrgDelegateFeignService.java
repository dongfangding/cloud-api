package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/orgDelegate")
public interface OrgDelegateFeignService {

    /**
     * 返回当前用户的操作公司列表，如果有激活的操作公司，则覆盖用户本身的部门和公司
     *
     * @param uid     用户uid
     * @param sysCode 系统代码
     * @return 返回当前用户的操作公司列表，如果有激活的操作公司，则覆盖用户本身的部门和公司
     */
    @RequestMapping("/userDelegate/{uid:.+}/{sysCode:.+}")
    Map<String, Object> getUserDelegate(
            @PathVariable(value = "uid") String uid, @PathVariable(value = "sysCode") String sysCode);


    /**
     * 更改用户激活的操作公司
     *
     * @param uid     用户UID
     * @param uid     用户uid
     * @param orgId   公司id
     * @param groupId 部门id
     * @param sysCode 系统代码
     * @return
     */
    @RequestMapping("/updateUserDelegate")
    public Map<String, Object> updateUserDelegate(
            @RequestParam(value = "uid") String uid, @RequestParam(value = "orgId") String orgId,
            @RequestParam(value = "groupId") String groupId, @RequestParam(value = "sysCode") String sysCode);
}
