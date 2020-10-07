package com.bz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bz.common.entity.Result;
import com.bz.common.entity.TbElectricityDataEntity;
import com.bz.common.entity.TbWaterPressureEntity;
import com.bz.mapper.TbElectricityDataMapper;
import com.bz.mapper.TbWaterPressureMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class JinShiLiService {

    @Resource
    private TbWaterPressureMapper tbWaterPressureMapper;

    @Resource
    private TbElectricityDataMapper tbElectricityDataMapper;

    public Result getPressureInfo(String deviceCode){
        Result result = new Result<>();
        if(deviceCode.isEmpty()){
            putMessage(result,-1,"缺少参数");
            return result;
        }
        LambdaQueryWrapper<TbWaterPressureEntity> wrapper = new QueryWrapper<TbWaterPressureEntity>().lambda();
        wrapper.eq(TbWaterPressureEntity::getDeviceCode,deviceCode);
        wrapper.orderByDesc(TbWaterPressureEntity::getSampleTime);
        wrapper.last("limit 1");
        List<TbWaterPressureEntity> list = tbWaterPressureMapper.selectList(wrapper);
        if(list.size()<1){
            putMessage(result,1,"该设备编码无数据");
            return result;
        }
        result.setData(list);
        result.setTotal(list.size());
        putMessage(result,0,"成功");
        return result;
    }

    public Result getElectricityInfo(String deviceCode){
        Result result = new Result<>();
        if(deviceCode.isEmpty()){
            putMessage(result,-1,"缺少参数");
            return result;
        }
        LambdaQueryWrapper<TbElectricityDataEntity> wrapper = new QueryWrapper<TbElectricityDataEntity>().lambda();
        wrapper.eq(TbElectricityDataEntity::getDeviceCode,deviceCode);
        wrapper.orderByDesc(TbElectricityDataEntity::getSampleTime);
        wrapper.last("limit 1");
        List<TbElectricityDataEntity> list = tbElectricityDataMapper.selectList(wrapper);
        if(list.size()<1){
            putMessage(result,1,"该设备编码无数据");
            return result;
        }
        result.setData(list);
        result.setTotal(list.size());
        putMessage(result,0,"成功");
        return result;
    }

    public Result putMessage(Result result,Integer code,String message){
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
