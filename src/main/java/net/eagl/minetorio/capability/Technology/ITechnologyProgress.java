package net.eagl.minetorio.capability.Technology;

import java.util.Set;

public interface ITechnologyProgress {
    Set<String> getLearnedTechnologies();
    void learnTechnology(String id);
    boolean hasLearned(String id);
    void setLearnedTechnologies(Set<String> techs);
}
