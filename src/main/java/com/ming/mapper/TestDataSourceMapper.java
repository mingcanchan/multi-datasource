package com.ming.mapper;

import com.ming.pojo.CityNoDO;
import org.springframework.stereotype.Repository;

/**
 * @author chenmingcan
 */
@Repository
public interface TestDataSourceMapper {

    /**
     * 获取code
     *
     * @param code code
     * @return codeDO
     */
    CityNoDO get(String code);

}
