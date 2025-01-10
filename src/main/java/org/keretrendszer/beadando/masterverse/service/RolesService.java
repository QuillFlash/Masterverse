package org.keretrendszer.beadando.masterverse.service;
import org.keretrendszer.beadando.masterverse.model.Roles;
import org.keretrendszer.beadando.masterverse.repository.IRolesRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolesService
{
    private final IRolesRepository iRolesRepository;

    public RolesService(IRolesRepository iRolesRepository)
    {
        this.iRolesRepository = iRolesRepository;
    }

    public List<Roles> getAllRoles()
    {
        return iRolesRepository.findAll();
    }

    public Roles getRoleById(long id)
    {
        return iRolesRepository.findById(id).orElse(null);
    }
}
