package com.pratik.major.configuration;

import com.pratik.major.model.Roles;
import com.pratik.major.model.User;
import com.pratik.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class GoogleOAuth2successHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;


    private RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();
    //it will be use for  redirecting  purpose if  we want  to redirect internally in spring
    /*@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }*/

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication; //this will send by google

        String email=token.getPrincipal().getAttributes().get("email").toString();

        if(userRepository.findByEmail(email).isPresent()==false)
        {
            User user= new User();
            user.setFirstName(token.getPrincipal().getAttributes().get("given_name").toString()); // given name=first name
            user.setLastName(token.getPrincipal().getAttributes().get("family_name").toString());
            user.setEmail(email);
            user.setRole(Roles.USER);
            userRepository.save(user);
            //this is  not required  any password  to store as it  is  authenticated by google
        }
        redirectStrategy.sendRedirect(httpServletRequest,httpServletResponse,"/");  //this will contains n redirecting  path  to home
    }
}
