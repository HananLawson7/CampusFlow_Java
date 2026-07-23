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

    // Changed from findAll() to match your repository's exact method name
    public List<Resource> getAllResources() {
        return resourceRepository.getAllResources();
    }

    public List<Resource> getResourcesByType(String type) {
        if (type == null) return List.of();

        return resourceRepository.getAllResources().stream()
                .filter(r -> r.getType().equalsIgnoreCase(type.trim()))
                .collect(Collectors.toList());
    }

    public List<Resource> getInstantlyBookableResources() {
        return resourceRepository.getAllResources().stream()
                .filter(r -> !r.isRequiresApproval())
                .collect(Collectors.toList());
    }
}