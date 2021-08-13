package com.epam.esm.service;

import com.epam.esm.entity.GitTag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitTagService {


    private final TagRepository tagRepo;

    @Autowired
    public GitTagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    public List<GitTag> getAllTags() {
        return tagRepo.getAll();
    }


    public void deleteById(long id) {
        tagRepo.deleteById(id);
    }
}
