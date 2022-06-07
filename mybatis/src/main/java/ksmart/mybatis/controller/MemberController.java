package ksmart.mybatis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksmart.mybatis.dto.Member;
import ksmart.mybatis.dto.MemberLevel;
import ksmart.mybatis.service.MemberService;

@Controller
@RequestMapping("/member") //
public class MemberController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);

	
	//DI (의존성 주입)
	//3. 생성자 메서드 주입방식
	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	
	@GetMapping("/sellerInfoList")
	public String getSellerInfoList(Model model) {
		List<Member> sellerInfoList = memberService.getSellerInfoList();	
		
		model.addAttribute("sellerInfoList", sellerInfoList);	
		
		log.info("sellerInfoList: {}", sellerInfoList);		
		
		return "member/sellerList";
	}
	
	@GetMapping("/memberInfoList")
	public String getMemberInfoList(Model model) {
		
		List<Member> memberInfoList = memberService.getMemberInfoList();
		
		model.addAttribute("memberInfoList", memberInfoList);
		
		return "member/memberInfoList";
	}
	
	
	@PostMapping("/memberList")
	public String getSearchMemberList(@RequestParam(name="searchKey") String searchKey
									,@RequestParam(name="searchValue", required=false) String searchValue
									,Model model) {
		log.info("searchKey : {}", searchKey);
		log.info("searchValue : {}", searchValue);
	
		if("memberId".equals(searchKey)) {
			searchKey = "m.m_id";
		}else if("memberLevel".equals(searchKey)) {
			searchKey = "m.m_level";
		}else if("memberName".equals(searchKey)) {
			searchKey = "m.m_name";
		}else {
			searchKey = "m.m_email";
		}
		List<Member> searchMemberList = memberService.getSearchMemberList(searchKey, searchValue);
		
		if(searchMemberList != null) model.addAttribute("memberList", searchMemberList); //무조건 필요는 없지만 혹여나!
		
		return "member/memberList";
	}
	
	@PostMapping("/removeMember")
	public String removeMember(@RequestParam(name="memberId") String memberId
							  ,@RequestParam(name="memberPw") String memberPw
							  ,RedirectAttributes reAttr){ 
		
		//비밀번호 일치시 삭제 : true, 비밀번호 불일치시 : false
		boolean isremove = memberService.removeMember(memberId, memberPw);
		
		if(isremove) {
			//회원 탈퇴
			return "redirect:/member/memberList";			
		}else {
			reAttr.addAttribute("result", "입력하신 회원 정보가 일치하지 않습니다.");
		}
		
		//reAttr get방식으로 정상적으로 보냈던 방식으로 노출될수있게 해준다. (memberId)
		//리디렉션 주소요청 시 ex) /member/removeMember?memberId=id001 
		reAttr.addAttribute("memberId", memberId);
		
		//ex) /member/removeMember?memberId=id001&result=입력하신+회원+정보가+일치하지 않습니다.
		return "redirect:/member/removeMember";
	}
	
	@GetMapping("/removeMember")
	public String removeMember(Model model
							  ,@RequestParam(name="memberId", required = false) String memberId
							  ,@RequestParam(name="result", required = false) String result) {
		
		model.addAttribute("memberId", memberId);
		
		if(result != null) model.addAttribute("result", result);
		
		return "member/removeMember";
	}
	
	@PostMapping("/idCheck")
	@ResponseBody 
	public boolean idCheck(@RequestParam(name="memberId") String memberId) {
		log.info("아이디 중복체크: {}", memberId);
		
		//true : 아이디중복 X, false : 아이디중복 O
		boolean isIdCheck = true;
		
		Member member = memberService.getMemberInfoById(memberId);
		
		if(member != null) {
			isIdCheck = false;
		}
		//List<Member> memberList = memberService.getMemberList();
		
		/*Member member = new Member();
		member.setMemberId(memberId);
		member.setMemberPw("pw001");
		member.setMemberName("홍01");*/
		
		return isIdCheck;
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		
		log.info("수정화면에서 입력받은 data: {}", member);
		
		memberService.modifyMember(member);
		
		return "redirect:/member/memberList";
	}
	
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(name="memberId", required=false) String memberId
								,Model model) {
		
		log.info("화면에서 입력받은 data: {}", memberId);
		Member member = memberService.getMemberInfoById(memberId);
		List<MemberLevel> memberLevelList = memberService.getMemberLevelList();
		
		model.addAttribute("member", member);
		model.addAttribute("memberLevelList", memberLevelList);
		
		return "member/modifyMember";
		//return "redirect:/";
	}
	
	/**
	 * 커멘드 객체 : http 통신 시 data(key, value) -> DTO(멤버변수와 일치시) 자동으로 바인딩하는 객체 
	 * String memberId = request.getParameter("memberId");
	 * Member member = new member();
	 * member.setMemberId(memberID);
	 * @RequestParam(name="memberId", required, defaultValue, value) String memberId (==) String memberId = request.getParameter("memberId");
	 * @param member member (커맨드객체) 
	 * @return Controller (String) "redirect:/" == response.sendRedirect("/") 와 같다. (알아서 객체를 생성, 바인딩해준다. =커맨드객체) 
	 */
	
	@PostMapping("/addMember")
	public String addMember(Member member
							,@RequestParam(name="memberId", required=false) String memberId 
							//required=false 매개변수를 필수적으로 받을건지 결정(안써도된다), defaultValue->기본값으로 세팅할때 
							,HttpServletRequest request) {
		log.info("회원가입 화면에서 입력한 data : {}", member);
		log.info("회원가입 화면에서 입력한 memberId : {}", memberId);
		
		memberService.addMember(member);
		
		return "redirect:/member/memberList";	
	}

	
	
	@GetMapping("/addMember")
	public String addMember(Model model) {
	
		List<MemberLevel> memberLevelList = memberService.getMemberLevelList();	
		model.addAttribute("memberLevelList", memberLevelList);	
		
		return "member/addMember";
	}
	
	/**
	 * http://localhost/member/MemberList
	 * @param model
	 * @return
	 */
	
	@GetMapping("/memberList")
	public String getMemberList(Model model) {
		
		List<Member> memberList = memberService.getMemberList();
		log.info("회원전체 목록: {}", memberList);
		model.addAttribute("memberList", memberList);
		
		return "member/memberList";
	}
}
