package org.makka.greenfarm.domain;

import lombok.Data;

@Data
public class GiteeUser {

    private Integer id; //NOT NULL
    private String name; //NOT NULL
    private String email; //可能为空
    //get、set方法
}
