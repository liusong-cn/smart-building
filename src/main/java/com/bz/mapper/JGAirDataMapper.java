package com.bz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bz.common.entity.JGAirData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author:ls
 * @date: 2020/11/12 11:47
 **/
public interface JGAirDataMapper extends BaseMapper<JGAirData> {

    public List<JGAirData> recentAirData(@Param("stationCode") String stationCode);

    public List<JGAirData> airDataHistory(@Param("stationCode") String stationCode,@Param("offset") int offset,
                                          @Param("size") int size);
}
