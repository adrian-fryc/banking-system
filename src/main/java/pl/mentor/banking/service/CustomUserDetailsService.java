package pl.mentor.banking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.mentor.banking.model.entity.User;
import pl.mentor.banking.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Próba logowania dla użytkownika: " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Nie znaleziono w bazie: " + username);
                    return new UsernameNotFoundException("User not found");
                });
        System.out.println("Znaleziono użytkownika, ID w bazie: " + user.getId());
        System.out.println("Znaleziono użytkownika, hasło w bazie: " + user.getPassword());
        return user;
    }

}
