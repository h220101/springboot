package ksmart.mybatis.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.service.GoodsService;



@Controller
@RequestMapping("/goods") //
public class GoodsController {
	
	private static final Logger log = LoggerFactory.getLogger(GoodsController.class);

	
	//DI (의존성 주입)
	//3. 생성자 메서드 주입방식
	private final GoodsService goodsService;
	
	public GoodsController(GoodsService goodsService) {
		this.goodsService = goodsService;
	}
	
	
	/*
	 * @GetMapping("/addMember") public String addMember(Model model) {
	 * 
	 * List<MemberLevel> memberLevelList = memberService.getMemberLevelList();
	 * model.addAttribute("memberLevelList", memberLevelList);
	 * 
	 * return "member/addMember"; }
	 */
	
	@GetMapping("/addGoods")
	public String addGoods(Model model) {
	
		List<Goods> goodsList = goodsService.getGoodsList();	
		model.addAttribute("goodsList", goodsList);	
		
		return "goods/addMember";
	}

	@GetMapping("/goodsList")
	public String getGoodsList(Model model) {
		
		List<Goods> goodsList = goodsService.getGoodsList();
		log.info("상품전체 목록: {}", goodsList);
		model.addAttribute("goodsList", goodsList);
		
		return "goods/goodsList";
	}
}
