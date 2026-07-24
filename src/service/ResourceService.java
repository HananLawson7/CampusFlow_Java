package service;

import model.Resource;
import repository.ResourceRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.getAllResources();
    }

    // 🎯 NEW: Direct pipeline to stream designated department assets straight to the UI
    public List<Resource> getResourcesByDepartment(String department) {
        if (department == null || department.isBlank()) return List.of();
        return resourceRepository.getResourcesByDepartment(department.trim());
    }

    public List<Resource> getResourcesByType(String type) {
        if (type == null || type.isBlank()) return List.of();

        return resourceRepository.getAllResources().stream()
                .filter(r -> r.getType() != null && r.getType().equalsIgnoreCase(type.trim()))
                .collect(Collectors.toList());
    }

    public List<Resource> getInstantlyBookableResources() {
        return resourceRepository.getAllResources().stream()
                .filter(r -> !r.isRequiresApproval())
                .collect(Collectors.toList());
    }
}