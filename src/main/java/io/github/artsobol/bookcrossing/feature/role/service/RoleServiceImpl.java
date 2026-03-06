package io.github.artsobol.bookcrossing.feature.role.service;

import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.role.entity.Role;
import io.github.artsobol.bookcrossing.feature.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("role.not.found")
        );
    }
}
