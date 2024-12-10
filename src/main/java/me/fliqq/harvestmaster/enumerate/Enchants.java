package me.fliqq.harvestmaster.enumerate;

public enum Enchants {
    //REGENERATION("Regeneration", "Restores hoe's energy", 3),
    FORTUNE("Fortune", "Increases the yield of harvested crops.", 1000),
    WEALTH("Wealth", "Multiplies the amount earned from selling crops.", 1000),
    EFFICIENCY("Efficiency", "Increases the speed of harvesting.", 5),
    //AUTO_REPLANT("Auto-Replant", "Automatically replants crops after harvesting.", 1),
    LUCK("Luck", "Increases the chance of finding rare items while farming.", 1000);

    private final String name;
    private final String description;
    private final int maxLevel;

    Enchants(String name, String description, int maxLevel) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
