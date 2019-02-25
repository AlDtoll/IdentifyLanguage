package com.example.identifylanguage.model;

import java.util.List;

/**
 * Ответ приходящий от сервера, содержит в себе список языков {@link com.example.identifylanguage.model.Language}
 */
public class Languages {

    private List<Language> languages;

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
}
