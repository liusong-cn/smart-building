package com.bz.service;

import com.SmartApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartApplication.class)
class ZhaTuBaoServiceTest {

    @Autowired
    private ZhaTuBaoService zhaTuBaoService;

    @Test
    void saveWarningInfo() {
        zhaTuBaoService.saveWarningInfo();
    }

    @Test
    void saveCarBaseInfo() {
        zhaTuBaoService.saveCarBaseInfo();
    }

    @Test
    void saveCarGPS(){
        zhaTuBaoService.saveRecentGPS();
    }
}