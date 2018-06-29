package com.ming.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author chenmingcan
 */
@Data
public class CityNoDO {

    private Integer id;
    private String code;
    private String city;
    private String province;
    private String insertTime;
    private Date updateTime;
}
