package ru.example.socnetwork.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.Person;
import ru.example.socnetwork.service.PersonService;

import java.util.List;

@Service
@RequiredArgsConstructor
@DebugLogs
public class UserDetailsServiceImpl implements UserDetailsService {

  private final PersonService personRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Person person = personRepository.getByEmail(email);
    if (person == null) {
      throw new UsernameNotFoundException("user " + email + " "
              + "not found");
    }
    return setUserDetails(person);
  }

  public SecurityUser setUserDetails(Person person) {
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("USER");
    return SecurityUser.builder()
            .id(person.getId())
            .username(person.getEmail())
            .password(person.getPassword())
            .authorities(authorities)
            .build();
  }
}
