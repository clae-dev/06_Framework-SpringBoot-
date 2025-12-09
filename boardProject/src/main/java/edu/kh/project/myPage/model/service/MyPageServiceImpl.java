package edu.kh.project.myPage.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MyPageServiceImpl implements MyPageService {

	@Autowired
	private MyPageMapper mapper;

	// BCrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {

		// 입력된 주소가 있을 경우
		// A^^^B^^^C 형태로 가공

		// 주소가 입력되었을 때
		if (!inputMember.getMemberAddress().equals(",,")) {
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);

		} else {
			// 주소가 입력되지 않았을 때
			inputMember.setMemberAddress(null);
		}

		// inputMember : 수정 닉네임, 수정 전화번호, 수정 주소,
		// 회원 번호

		return mapper.updateInfo(inputMember);
	}

	// 비밀번호 변경 서비스
	@Override
	public int changePw(Map<String, Object> paramMap, int memberNo) {

		// 1. 현재 비밀번호가 일치하는지 확인하기
		// - 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String originPw = mapper.selectPw(memberNo);

		// 입력받은 현재 비밀번호와(평문)
		// DB에서 조회한 비밀번호(암호화)를 비교
		// -> BCryptPasswordEncoder.matches(평문, 암호화된비밀번호) 사용

		// 다를 경우
		if (!bcrypt.matches((String) paramMap.get("currentPw"), originPw)) {
			return 0;
		}

		// 2. 같을경우

		// 새 비밀번호를 암호화 (BCryptPasswordEncoder.encode(평문))
		String encPw = bcrypt.encode((String) paramMap.get("newPw"));

		// 진행후 DB에 업데이트
		// SQL 전달해야하는 데이터 2개 (암호화한 새 비밀번호, 회원번호)
		// -> SQL 전달 인자 1개뿐!
		// -> 묶어서 전달 (paramMap 재활용)

		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo);

		return mapper.changePw(paramMap);
	}

}
