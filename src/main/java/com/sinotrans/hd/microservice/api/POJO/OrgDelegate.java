package com.sinotrans.hd.microservice.api.POJO;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgDelegate implements Serializable {
    @JsonProperty("ORG_ID")
    private String orgId;
    @JsonProperty("ORG_NAME")
    private String orgName;
    @JsonProperty("ORG_CODE")
    private String orgCode;
    @JsonProperty("ORG_SNAME")
    private String orgSName;
    @JsonProperty("GROUP_ID")
    private String groupId;
    @JsonProperty("GROUP_NAME")
    private String groupName;
    @JsonProperty("GROUP_CODE")
    private String groupCode;
    @JsonProperty("GROUP_EMAIL")
    private String groupEmail;
    @JsonProperty("ACTIVE")
    private String active;
    @JsonProperty("SYS_CODE")
    private String sysCode;

}
