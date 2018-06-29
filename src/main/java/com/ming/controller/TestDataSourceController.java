package com.ming.controller;

import com.ming.datasource.DatabaseType;
import com.ming.service.TestDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenmingcan
 */
@RestController
@RequestMapping("/test")
public class TestDataSourceController {

    private final TestDataSourceService testDataSourceService;

    @Autowired
    public TestDataSourceController(TestDataSourceService testDataSourceService) {
        this.testDataSourceService = testDataSourceService;
    }

    @GetMapping("/master")
    public Object getMasterData() {
        return testDataSourceService.getDataByDatasource(DatabaseType.master);
    }

    @GetMapping("/other")
    public Object getOtherData() {
        return testDataSourceService.getDataByDatasource(DatabaseType.other);
    }
}

