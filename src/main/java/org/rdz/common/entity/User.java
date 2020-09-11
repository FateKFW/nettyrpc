package org.rdz.common.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -6980418243356700855L;

    private String id;
    private String name;
    private Integer age;
}
