package ksmart.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.dto.Goods;

@Mapper
public interface GoodsMapper {

	public List<Goods> getGoodsList();
}
