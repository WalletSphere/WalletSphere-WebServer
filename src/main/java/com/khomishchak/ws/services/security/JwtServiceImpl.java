package com.khomishchak.ws.services.security;

import com.khomishchak.ws.model.enums.DeviceType;
import com.khomishchak.ws.security.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.jose4j.jwk.EcJwkGenerator;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {

    private Long jwtWebExpirationTimeInMinutes;
    private Long jwtAppExpirationTimeInMinutes;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ALG_HEADER = AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256;

    private Key privateKey;
    private Key publicKey;

    @Value("${ws.security.jwt.expiration-time.web.minutes:30}")
    public void setJwtWebExpirationTimeInMinutes(Long jwtWebExpirationTimeInMinutes) {
        this.jwtWebExpirationTimeInMinutes = jwtWebExpirationTimeInMinutes;
    }

    @Value("${ws.security.jwt.expiration-time.app.minutes:4320}")
    public void setJwtAppExpirationTimeInMinutes(Long jwtAppExpirationTimeInMinutes) {
        this.jwtAppExpirationTimeInMinutes = jwtAppExpirationTimeInMinutes;
    }

    @PostConstruct
    public void initKeys() {
        EllipticCurveJsonWebKey ecKey;
        try {
            ecKey = EcJwkGenerator.generateJwk(EllipticCurves.P256);
            this.privateKey = ecKey.getPrivateKey();
            this.publicKey = ecKey.getECPublicKey();
        } catch (JoseException e) {
            throw new RuntimeException("Error generating keys", e);
        }
    }

    @Override
    public String generateToken(Map<String, String> extraClaims, UserDetails userDetails, DeviceType deviceType) {
        UserDetailsImpl userDetailsImpl = getUserDetailsImpl(userDetails);

        Long expiryTime = deviceType.equals(DeviceType.WEB) ? jwtWebExpirationTimeInMinutes : jwtAppExpirationTimeInMinutes;

        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setSubject(userDetails.getUsername());
        jwtClaims.setClaim("userId", userDetailsImpl.getUserId());
        jwtClaims.setIssuedAtToNow();
        jwtClaims.setExpirationTimeMinutesInTheFuture(expiryTime);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmHeaderValue(ALG_HEADER);
        jws.setKey(privateKey);
        jws.setPayload(jwtClaims.toJson());

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException("Error generating JWT", e);
        }
    }

    private UserDetailsImpl getUserDetailsImpl(UserDetails userDetails) {
        if(!(userDetails instanceof UserDetailsImpl)) {
            throw new IllegalArgumentException("Expected type was UserDetailsImpl");
        }

        UserDetailsImpl userDetailsimpl = (UserDetailsImpl) userDetails;

        String username = userDetailsimpl.getUsername();
        Long userId = userDetailsimpl.getUserId();

        if(!StringUtils.hasText(username) || userId == null) {
            throw new InvalidParameterException("username or userId is empty, can not generate jwt token");
        }

        return userDetailsimpl;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {

        return isTokenExpired(token) && userDetails.getUsername().equals(extractUsername(token));
    }

    private boolean isTokenExpired(String token) {
        boolean tokenIsValid;
        try {
            tokenIsValid = extractAllClaims(token).getExpirationTime().isAfter(NumericDate.now());
        } catch (MalformedClaimException exception) {
            tokenIsValid = false;
            // logging
        }
        return tokenIsValid;
    }

    @Override
    public String extractUsername(String token) {
        String username;

        try {
            username = extractAllClaims(token).getSubject();
        } catch (MalformedClaimException | RuntimeException exception) {
            username = null;
            // logging
        }

        return username;
    }

    @Override
    public Long extractUserId(String token) {
        return (Long) extractAllClaims(token).getClaimValue("userId");
    }

    @Override
    public Optional<String> getToken(HttpServletRequest request) {
        String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtTokenHeader == null || !jwtTokenHeader.startsWith(TOKEN_PREFIX)) {
            return Optional.empty();
        }

        return Optional.of(jwtTokenHeader.substring(TOKEN_PREFIX.length()));
    }

    private JwtClaims extractAllClaims(String token) {

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireIssuedAt()
                .setRequireExpirationTime()
                .setVerificationKey(publicKey)
                .build();

        JwtContext jwtContext;

        try{
            jwtContext = jwtConsumer.process(token);
        } catch (InvalidJwtException e) {
            throw new RuntimeException("Was not Able to process jwt token: %s", e);
        }

        return jwtContext.getJwtClaims();
    }
}
