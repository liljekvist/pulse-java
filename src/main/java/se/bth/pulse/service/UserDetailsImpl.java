package se.bth.pulse.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.bth.pulse.entity.Authority;
import se.bth.pulse.entity.User;

/**
 * This class is used by spring security to authenticate and authorize user. It implements
 * UserDetails interface to change the behaviour of getAuthorities method. It also is used to change
 * the underlying class to our User entity.
 */
public class UserDetailsImpl implements UserDetails {

  private final User user;

  public UserDetailsImpl(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<Authority> auths = user.getRole().getAuthorities();
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    for (Authority auth : auths) {
      authorities.add(new SimpleGrantedAuthority(auth.getName()));
    }

    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }


  public User getUser() {
    return user;
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean userShouldChangePassword() {
    return user.getCredentialsExpired();
  }

  @Override
  public boolean isEnabled() {
    return user.getEnabled();
  }

}
