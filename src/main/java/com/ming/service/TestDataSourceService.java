package com.ming.service;

import com.ming.datasource.DatabaseType;
import com.ming.datasource.DynamicDataSource;
import com.ming.mapper.TestDataSourceMapper;
import com.ming.pojo.CityNoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenmingcan
 */
@Service
public class TestDataSourceService {

    private final TestDataSourceMapper testDataSourceMapper;

    @Autowired
    public TestDataSourceService(TestDataSourceMapper testDataSourceMapper) {
        this.testDataSourceMapper = testDataSourceMapper;
    }

    public CityNoDO getDataByDatasource(DatabaseType databaseType) {
        DynamicDataSource.setDatabaseType(databaseType);
        return testDataSourceMapper.get("010");
    }

}
