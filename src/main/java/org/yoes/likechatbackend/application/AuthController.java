package org.yoes.likechatbackend.application;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.services.Impl.AuthenticationServiceToken;
import org.yoes.likechatbackend.domain.util.security.token.Impl.AuthenticationService;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationServiceToken authenticationServiceToken;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> credentials)
            throws Exception {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            String token = authenticationService.authenticate(email, password);
            return ResponseEntity.ok(new AuthenticationResponse() {
                @Override
                public boolean indicatesSuccess() {
                    return false;
                }

                @Override
                public HTTPResponse toHTTPResponse() {
                    return null;
                }

                @Override
                public URI getRedirectionURI() {
                    return null;
                }

                @Override
                public State getState() {
                    return null;
                }

                @Override
                public AuthenticationSuccessResponse toSuccessResponse() {
                    return null;
                }

                @Override
                public AuthenticationErrorResponse toErrorResponse() {
                    return null;
                }
            });
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = authenticationService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String resetToken, String email,
                                           @RequestParam("newPassword") String newPassword) {
        try {
            authenticationServiceToken.resetPassword(resetToken, email, newPassword);
            return ResponseEntity.ok("Contraseña restablecida correctamente");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
