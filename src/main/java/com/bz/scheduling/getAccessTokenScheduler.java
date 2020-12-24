package com.bz.scheduling;

import com.bz.redis.service.RedisService;
import com.bz.service.ZhaTuBaoService;
import com.bz.utils.AccessTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Component
public class getAccessTokenScheduler {
    @Value("${zhatubao.username}")
    private String username;

    @Value("${zhatubao.appsecret}")
    private String appsecret;

    @Value("${zhatubao.getAuthTokenUrl}")
    private String authTokenUrl;

    @Autowired
    private ZhaTuBaoService zhaTuBaoService;

    @Autowired
    private RedisService redisService;

//    @Scheduled(fixedRate=86000000)
    public void getAccessTokenFromRemote() {
        String token = zhaTuBaoService.getToken(username, appsecret, authTokenUrl);
        AccessTokenUtil.accessToken.setAccess_token(token);
    }


}
