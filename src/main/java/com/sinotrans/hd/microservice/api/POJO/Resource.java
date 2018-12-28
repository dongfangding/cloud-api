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
public class Resource implements Serializable {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("CODE")
    private String code;
    @JsonProperty("TYPE")
    private String type;
    @JsonProperty("PARENT_ID")
    private String parentId;
    @JsonProperty("SEQ")
    private Integer seq;

}
