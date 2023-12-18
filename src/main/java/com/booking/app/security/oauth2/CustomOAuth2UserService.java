package com.booking.app.security.oauth2;


import com.booking.app.entity.UserProvider;
import com.booking.app.entity.UserSecurity;
import com.booking.app.exception.exception.OAuth2AuthenticationProcessingException;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserProviderRepository;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.security.oauth2.user.OAuth2UserInfo;
import com.booking.app.security.oauth2.user.OAuth2UserInfoFactory;
import com.booking.app.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserSecurityRepository userSecurityRepository;
    private final UserProviderRepository userProviderRepository;
    private final RoleRepository roleRepository;
    private final RegistrationService registrationService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oauth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(oauth2UserRequest);

        try {
            return processOAuth2User(oauth2UserRequest, oauth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oauth2UserRequest, OAuth2User oauth2User) {
        OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(oauth2UserRequest.getClientRegistration().getRegistrationId(), oauth2User.getAttributes());

        if (StringUtils.isEmpty(oauth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<UserSecurity> userOptional = userSecurityRepository.findByEmail(oauth2UserInfo.getEmail());

        UserSecurity user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user = updateExistingUser(user, oauth2UserInfo);
        } else {
            user = registerNewUser(oauth2UserRequest, oauth2UserInfo);
        }

        return UserPrincipal.create(user, oauth2User.getAttributes());
    }

    private UserEntity registerNewUser(OAuth2UserRequest oauth2UserRequest, OAuth2UserInfo oauth2UserInfo) {

        UserProvider provider = new UserProvider();
        provider.setName(oauth2UserRequest.getClientRegistration().getRegistrationId());
        provider.setUserIdByProvider(oauth2UserInfo.getId());
        UserEntity user = new UserEntity();
        StringToNameParser.setUserNameFromRequest(oauth2UserInfo, user);
        provider.setEmail(oauth2UserInfo.getEmail());
        user.setEmail(oauth2UserInfo.getEmail());
        user.setAvatar(oauth2UserInfo.getImageUrl());
        user.setStatus(UserStatus.NEW);
        Optional<RoleEntity> roleEntity = roleRepository.getRoleEntityByName("Doctor");
        user.setRole(roleEntity.orElse(null));
        user.setEnabled(true);
        UserEntity savedUser = userSecurityRepository.save(user);
        provider.setUser(savedUser);
        userProviderRepository.save(provider);
        return savedUser;
    }

    private UserEntity updateExistingUser(UserEntity existingUser, OAuth2UserInfo oauth2UserInfo) {
        existingUser.setFirstName(oauth2UserInfo.getName().split(" ")[0]);
        existingUser.setLastName(oauth2UserInfo.getName().split(" ")[1]);
        existingUser.setAvatar(oauth2UserInfo.getImageUrl());
        return userSecurityRepository.save(existingUser);
    }

}
