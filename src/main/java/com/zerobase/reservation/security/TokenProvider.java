package com.zerobase.reservation.security;

import com.zerobase.reservation.constants.Authority;
import com.zerobase.reservation.service.MemberService;
import com.zerobase.reservation.service.PartnerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30min
    private static final String KEY_ROLES = "roles";

    private static final String MEMBER = Authority.ROLE_MEMBER.name();
    private static final String PARTNER = Authority.ROLE_PARTNER.name();

    private final MemberService memberService;
    private final PartnerService partnerService;

    @Value("{spring.jwt.secret-key}")
    private String secretKey;


    public String generateToken(String name, Set<String> roles){
        Claims claims = Jwts.claims().setSubject(name);
        claims.put(KEY_ROLES, roles);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();

    }

    public Authentication getAuthentication(String jwt) {
        String roles = getKeyRoles(jwt);
        System.out.println("!!!!!!!!!!!!!!!!!!! = " + jwt);

        UserDetails userDetails = null;
        if(roles.contains(MEMBER)){
            userDetails = memberService.loadUserByUsername(getUsername(jwt));
        } else if(roles.contains(PARTNER)) {
            userDetails = partnerService.loadUserByUsername(getUsername(jwt));
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getKeyRoles(String token) {
        System.out.println("!!!!!!!!!!!!!!!!!!! = " + parseClaims(token).get(KEY_ROLES).toString());

        return parseClaims(token).get(KEY_ROLES).toString();
    }

    public String getUsername(String token) {
        System.out.println("!!!!!!!!!!!!!!!!!!! = " + parseClaims(token).getSubject());

        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)) return false;

        var claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token){
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
