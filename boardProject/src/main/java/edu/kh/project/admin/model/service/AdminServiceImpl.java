package edu.kh.project.admin.model.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.admin.model.mapper.AdminMapper;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

   private final AdminMapper mapper;
   private final BCryptPasswordEncoder bCrypt;
   
   // 관리자 로그인 서비스
   @Override
   public Member login(Member inputMember) {
      Member loginMember = mapper.login(inputMember.getMemberEmail());
      
      if(loginMember == null) return null;
      
      if(!bCrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
         return null;
      }
      
      loginMember.setMemberPw(null);
      
      return loginMember;
   }

}
