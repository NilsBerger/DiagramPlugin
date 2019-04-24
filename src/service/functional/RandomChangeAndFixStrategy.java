package service.functional;

import materials.ProgramEntity;

import java.util.Set;

public class RandomChangeAndFixStrategy implements ChangeAndFixStrategyIF {
    @Override
    public boolean accept(Set<ProgramEntity> affectedClasses) {
        return true;
    }
}
