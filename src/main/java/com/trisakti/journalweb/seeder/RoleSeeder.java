package com.trisakti.journalweb.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.trisakti.journalweb.model.ERole;
import com.trisakti.journalweb.model.Role;
import com.trisakti.journalweb.repository.RoleRepository;

@Component
public class RoleSeeder implements ApplicationRunner {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedRoleIfNotExists(ERole.ROLE_USER);
        seedRoleIfNotExists(ERole.ROLE_ADMIN);
    }
    
    private void seedRoleIfNotExists(ERole roleName) {
        Optional<Role> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isEmpty()) {
            Role newRole = new Role(roleName);
            roleRepository.save(newRole);
        }
    }
}
