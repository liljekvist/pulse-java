package se.bth.pulse.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import se.bth.pulse.service.UserDetailsImpl;

/**
 * This class is used to redirect the user to the change password page if the user is logging in for
 * the first time. It uses the AuthenticationSuccessHandler interface to change the behaviour of
 * onAuthenticationSuccess method. It implements the interface to override the method. The method is
 * invoked from ApplicationSecurityConfig class.
 */
public class PasswordChangeAuthenticationSuccessHandler implements
    AuthenticationSuccessHandler {

  private final AuthenticationSuccessHandler target =
      new SavedRequestAwareAuthenticationSuccessHandler();

  /**
   * This method is used to redirect the user to the change password page if the user is logging in
   * for the first time. It gets the username of the logged-in user from the authentication object.
   *
   * @param request  - used to pass on any request after a successful login
   * @param response - used to redirect the user to the change password page if the user is logging
   *                 in for the first time
   * @param auth     - used to get the username of the logged-in user
   * @throws IOException      - is thrown from the target onAuthenticationSuccess method.
   * @throws ServletException - is thrown from the target onAuthenticationSuccess method.
   */
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response, Authentication auth) throws IOException, ServletException {

    UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

    if (user.userShouldChangePassword()) {
      response.sendRedirect("/change-password");
    } else {
      target.onAuthenticationSuccess(request, response, auth);
    }
  }

  public void proceed(HttpServletRequest request,
      HttpServletResponse response, Authentication auth) throws ServletException, IOException {
    target.onAuthenticationSuccess(request, response, auth);
  }
}
