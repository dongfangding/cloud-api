package com.sinotrans.hd.microservice.api.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.Serializable;
import java.util.List;

/**
 * @author DDf on 2018/10/9
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("U_ID")
    private String uid;
    @JsonProperty("USER_ID")
    private String userId;
    @JsonProperty("USER_NAME")
    private String userName;
    @JsonProperty("USER_CODE")
    private String userCode;
    @JsonProperty("USER_EMAIL")
    private String userEmail;
    @JsonProperty("USER_ROLE_CODE")
    private String userRoleCode;
    @JsonProperty("USER_ROLE_NAME")
    private String userRoleName;
    @JsonProperty("ORG_ID")
    private String orgId;
    @JsonProperty("ORG_NAME")
    private String orgName;
    @JsonProperty("ORG_SNAME")
    private String orgSName;
    @JsonProperty("ORG_CODE")
    private String orgCode;
    @JsonProperty("GROUP_ID")
    private String groupId;
    @JsonProperty("GROUP_CODE")
    private String groupCode;
    @JsonProperty("GROUP_NAME")
    private String groupName;
    @JsonProperty("GROUP_EMAIL")
    private String groupEmail;
    @JsonProperty("MESSAGERURL")
    private String messagerurl;

    @JsonProperty("T_O_ORG_DELEGATE")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrgDelegate> orgDelegateList;
    @JsonProperty("T_O_RESOURCE")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Resource> resourceList;
    @JsonProperty("T_O_DATA_AUTHORITY")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DataAuthority> dataAuthorityList;
}
