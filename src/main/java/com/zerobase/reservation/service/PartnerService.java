package com.zerobase.reservation.service;

import com.zerobase.reservation.dto.AuthPartner;
import com.zerobase.reservation.dto.PartnerDto;
import com.zerobase.reservation.exception.impl.AlreadyExistUsernameException;
import com.zerobase.reservation.exception.impl.UserNotFoundException;
import com.zerobase.reservation.exception.impl.UsernameOrPasswordNotMatchException;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.repository.entity.Partner;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartnerService implements UserDetailsService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    
    // 파트너 회원 가입
    @Transactional
    public PartnerDto register(AuthPartner.SignUp partnerRequest) {
        // 입력한 파트너 아이디(name)을 가져와 같은 아이디가 있다면 에러 발생
        boolean exists = this.partnerRepository.existsByUsername(partnerRequest.getUsername());
        if (exists) throw new AlreadyExistUsernameException();

        partnerRequest.setPassword(this.passwordEncoder.encode(partnerRequest.getPassword()));
        return PartnerDto.fromEntity(partnerRepository.save(partnerRequest.toEntity()));
    }

    // 로그인시 인증
    public Partner authenticate(AuthPartner.SignIn partnerRequest){

        Partner partner = partnerRepository.findByUsername(partnerRequest.getUsername())
                .orElseThrow(() -> new UsernameOrPasswordNotMatchException());

        if (!passwordEncoder.matches(partnerRequest.getPassword(), partner.getPassword())) {
            throw new UsernameOrPasswordNotMatchException();
        }

        return partner;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return partnerRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
