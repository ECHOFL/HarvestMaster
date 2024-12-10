package me.fliqq.harvestmaster.object;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.fliqq.harvestmaster.enumerate.Enchants;

@AllArgsConstructor
@Getter
public class PersonalHoe {
    private final UUID uuid;
    private int level;
    private int prestige;
    @Setter
    private Map<Enchants, Integer> enchants;
    
    //init constructor
    public PersonalHoe(UUID uuid){
        this.uuid=uuid;
    }
}
