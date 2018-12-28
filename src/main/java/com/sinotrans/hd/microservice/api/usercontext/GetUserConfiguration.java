package com.sinotrans.hd.microservice.api.usercontext;

import lombok.Getter;
import lombok.Setter;

/**
 * @author DDf on 2018/11/2
 * 请勿手动将该类注册到容器中
 */
public class GetUserConfiguration {
    public static final String BEAN_NAME = "getUserConfiguration";

    @Getter
    @Setter
    private boolean enableGetUser;
    /**
     * {@link EnableGetUser#globalShowDetails()}
     */
    @Getter
    @Setter
    private boolean globalShowDetails;
}
