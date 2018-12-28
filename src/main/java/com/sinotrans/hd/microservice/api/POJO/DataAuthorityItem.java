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
public class DataAuthorityItem implements Serializable {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DATA_AUTH_ID")
    private String dataAuthId;
    @JsonProperty("AUTH_CODE")
    private String authCode;
    @JsonProperty("AUTH_NAME")
    private String authName;

}
