package club.management.club.features.services.users;

import club.management.club.features.entities.Authority;
import club.management.club.features.entities.User;
import club.management.club.features.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
//Only includes final fields and fields with @NonNull annotations in the constructor.
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email).
                orElseThrow(() ->new UsernameNotFoundException("User not found"));
    }
    public Optional<User> findByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }
    public Optional<User> findUserById(String id){
        return userRepo.findUserById(id);
    }
    public User saveUser(User user) {
        return userRepo.save(user);
    }
    public List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {

        return authorities.stream().
                map(authority -> new SimpleGrantedAuthority(authority.getName())).
                collect(Collectors.toList());
    }
    public void deleteUserById(String id){
         userRepo.deleteById(id);
    }
    public String getFullName(User user){
        return user.getFirstName()+" "+ user.getLastName();
    }
}
