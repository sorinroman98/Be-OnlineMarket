package com.springapp.springjwt.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;

import static com.springapp.springjwt.constant.SecurityConstant.*;
import static java.util.Arrays.stream;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.springapp.springjwt.domain.UserPrincipal;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JWTTokenProvider {

   // @Value("${jwt.secret}")
    private String secret ="secret";
    public static String getPrincipal() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getPrincipal().toString();
        } else {
            throw new JWTVerificationException("User is not authenticated!");
        }

    }

    public String generateJwtToken(UserPrincipal userPrincipal){
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES,claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(  Algorithm.HMAC512(secret));
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username,null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(String username,String token){
        JWTVerifier verifier = getJWTVerifier();

        return StringUtils.isNotEmpty(username) && !isTokenExpired(token,verifier);
    }

    public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();

        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(String token, JWTVerifier verifier) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
        }catch (JWTVerificationException verificationException){
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }

        return verifier;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority: userPrincipal.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
