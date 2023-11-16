package com.booking.app.security.jwt;

import com.booking.app.entity.UserSecurity;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtToUserSecurityConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken>{

    //this is not gonna work:)
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        source.
        UserSecurity userSecurity = new UserSecurity();

        return new UsernamePasswordAuthenticationToken(source., , Collections.EMPTY_LIST);
    }
}
