package ksmart.mybatis.service;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.mapper.GoodsMapper;


@Service
@Transactional
public class GoodsService {
   // DI (의존성 주입)
	
   /*
    * 1. 필드 주입 방식
       @Autowired
       private MemberMapper memberMapper;   
     */
   
   
   /*
    * 2. setter 메서드 주입 방식
      @Autowired
      private MemberMapper memberMapper;
      
      public void setMemberMapper(MemberMapper memberMapper) {
         this.memberMapper = memberMapper;
      }
   */
   
   
   /*
    * 3. 생성자 메서드 주입 방식
    * */
   private final GoodsMapper goodsMapper; //dao

   public GoodsService(GoodsMapper goodsMapper) {
      this.goodsMapper = goodsMapper;
   }
   
   public List<Goods> getGoodsList() {
	   List<Goods> goodsList = goodsMapper.getGoodsList();
	   return goodsList;
   }
}  