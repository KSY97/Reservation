package com.zerobase.reservation.service;

import com.zerobase.reservation.dto.AuthMember;
import com.zerobase.reservation.dto.MemberDto;
import com.zerobase.reservation.exception.impl.AlreadyExistUsernameException;
import com.zerobase.reservation.exception.impl.UserNotFoundException;
import com.zerobase.reservation.exception.impl.UsernameOrPasswordNotMatchException;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 멤버 회원 가입
    @Transactional
    public MemberDto register(AuthMember.SignUp memberRequest){
        // 입력한 멤버 아이디(name)을 가져와 같은 아이디가 있다면 에러 발생
        boolean exists = memberRepository.existsByUsername(memberRequest.getUsername());
        if (exists) throw new AlreadyExistUsernameException();

        memberRequest.setPassword(passwordEncoder.encode(memberRequest.getPassword()));
        return MemberDto.fromEntity(memberRepository.save(memberRequest.toEntity()));
    }

    // 로그인시 인증
    public Member authenticate(AuthMember.SignIn memberRequest){

        Member member = memberRepository.findByUsername(memberRequest.getUsername())
                .orElseThrow(() -> new UsernameOrPasswordNotMatchException());

        if (!passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())) {
            throw new UsernameOrPasswordNotMatchException();
        }

        return member;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
