package org.lla.languagefinder.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.lla.languagefinder.persistence.entities.Developer;
import org.lla.languagefinder.persistence.entities.Language;
import org.lla.languagefinder.persistence.entities.Project;
import org.lla.languagefinder.persistence.repositories.DeveloperRepository;
import org.lla.languagefinder.persistence.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
public class Controller {

    WebClient client = WebClient.create();

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    DeveloperRepository developerRepository;

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("/data")
    public void fetchData() {

        JsonNode[] developerResponse = client.get()
                .uri("https://api.github.com/orgs/codecentric/members")
                .retrieve()
                .bodyToMono(JsonNode[].class)
                .block();

        if (developerResponse == null) {
            return;
        }

        List<JsonNode> responseRepos = new ArrayList<>();
        Arrays.stream(developerResponse).forEach(dev -> responseRepos.addAll(
                Arrays.stream(Objects.requireNonNull(client
                        .get()
                        .uri(dev.path("repos_url").asText())
                        .retrieve()
                        .bodyToMono(JsonNode[].class)
                        .block())).toList())
        );
        responseRepos.forEach(this::persistProject);
    }

    @Transactional
    public void persistLanguages(Set<String> languages) {
        languages.stream().forEach(lang -> {
                    Language languageEntity = new Language();
                    languageEntity.setName(lang);
                    entityManager.merge(languageEntity);
                }
        );
    }

    @Transactional
    @PostMapping("/developers")
    public void persistDeveloper(@RequestBody JsonNode[] developers) {
        Arrays
                .stream(developers)
                .forEach(developer -> {
                    Developer devEntity = new Developer();
                    devEntity.setLogin(developer.path("login").asText());
                    entityManager.merge(devEntity);
                });
    }

    @Transactional
    @PostMapping("languages")
    public void persistLanguages(@RequestBody JsonNode languages) {
        languages.fieldNames().forEachRemaining(System.out::println);
        Language languageEntity = new Language();
        languageEntity.setName(languages.path("abc").asText());
        entityManager.merge(languageEntity);
    }

    @Transactional
    @PostMapping("project")
    public void persistProject(@RequestBody JsonNode repository) {
        Project project = new Project();

        Optional<Developer> owner = developerRepository.findById(repository.path("owner").path("login").asText());
        if (owner.isEmpty()) {
            return;
        }

        project.setDeveloper(owner.get());

        Set<String> languages = new HashSet<>();

        JsonNode languageResponse = client
                .get()
                .uri(repository.path("url").asText() + "/languages")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        languageResponse.fieldNames().forEachRemaining(languages::add);

        Set<Language> langEntities = new HashSet<>();
        languages.stream().forEach(lang -> {
            Language languageEntity = new Language();
            languageEntity.setName(lang);
            entityManager.merge(languageEntity);
            langEntities.add(languageEntity);
        });

        project.setLanguage(langEntities);
        // somehow relationships are not persisted
        entityManager.merge(project);
    }

    @GetMapping("/developers")
    public List<Developer> getDevelopers() {

        return developerRepository.findAll();
    }

}
