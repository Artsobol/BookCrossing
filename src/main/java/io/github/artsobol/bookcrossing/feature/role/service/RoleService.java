package io.github.artsobol.bookcrossing.feature.role.service;

import io.github.artsobol.bookcrossing.feature.role.entity.Role;

public interface RoleService {

    Role findByName(String name);
}
