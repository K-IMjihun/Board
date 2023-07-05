package com.board.board.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // JWT 데이터

    // Header KEY(cookie의 name) 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token식별자. 토큰 앞에 붙일 용어(구분을 위해 한칸 띄워야 함)
    public static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분


    // ${jwt.secret.key} = Application.properties 저장한 키값을 불러옴
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // 딱 한번만 받아오면 되는 값을 사용할 때 마다 새로운 요청을 방지하기 위해 사용
    // 생성자가 호출된 뒤에 호출이 됨
    @PostConstruct
    public void init() {
        // 값을 사용하기 위해 base64로 디코딩 하는 과정
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // 디코딩 된 값을 key로 반환
        key = Keys.hmacShaKeyFor(bytes);
    }
    // 토큰 생성
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT 토큰 substring(Bearer 의 공백 때문에 사용하지 않으면 에러발생)
   public String substringToken(@NotNull String tokenValue) {
        if (tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }
    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // parserBuilder(): JWT 파서생성. 이후 JWT를 파싱할 수 있다.
            // setSigningKey(key): 암호화할때 사용할 키
            // build(): JWT파서를 빌드하여 완전한 JWT 파서 객체 생성
            // parseClaimsJws(token): JWT의 유효성을 검증하고, JWT에 포함된 클레임 (claims)을 추출
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        // 검증 후 body 부분에 존재하는 claims(데이터 집합)를 가져옴
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        return req.getHeader(AUTHORIZATION_HEADER);
    }
}
