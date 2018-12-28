package com.sinotrans.hd.microservice.api.POJO;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataAuthority implements Serializable {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("USER_UID")
    private String userUid;
    @JsonProperty("TABLE_NAME")
    private String tableName;
    @JsonProperty("RESOURCE_CODE")
    private String resourceCode;
    @JsonProperty("OPERATE")
    private String operate;

    @JsonProperty("T_O_DATA_AUTHORITY_ITEM")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DataAuthorityItem> dataAuthorityItemList;

}
