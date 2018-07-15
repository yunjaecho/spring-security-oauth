package com.yunjae.springsecurityoauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer configure) throws Exception {
        configure
                .inMemory()
                .withClient("yunjae-client")    // 구글, 페이스북에서 생성하는 앱이름
                .secret(passwordEncoder.encode("yunjae-password"))  // 구글, 페이스북에서 자동발급되는 비밀번호 형태
                /**
                 * =======================================
                 * OAuth2 Grant Types
                 * =======================================
                 * Following are the 4 different grant types defined by OAuth2
                 * Authorization Code: used with server-side Applications
                 * Implicit: used with Mobile Apps or Web Applications (applications that run on the user's device)
                 * Resource Owner Password Credentials: used with trusted Applications, such as those owned by the service itself
                 * Client Credentials: used with Applications API access
                 */
                .authorizedGrantTypes("password",
                        "authorization_code",
                        "refresh_token",
                        "implicit")
                .scopes("read", "write", "trust")
                .accessTokenValiditySeconds(1*60*60)
                .refreshTokenValiditySeconds(6*60*60);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
            .authenticationManager(authenticationManager);
    }
}

