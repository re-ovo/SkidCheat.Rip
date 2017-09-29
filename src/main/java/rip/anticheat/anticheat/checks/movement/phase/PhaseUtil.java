/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package rip.anticheat.anticheat.checks.movement.phase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class PhaseUtil {
    private YamlConfiguration blocks;
    private Set<Material> passable;
    private Set<Material> doors;
    private Set<Material> trapdoors;
    private Set<Material> gates;
    private Set<Material> fences;

    public void load(YamlConfiguration yamlConfiguration) {
        this.blocks = yamlConfiguration;
        this.passable = this.getMaterials("passable");
        this.trapdoors = this.getMaterials("trapdoors");
        this.doors = this.getMaterials("doors");
        this.gates = this.getMaterials("gates");
        this.fences = this.getMaterials("fences");
    }

    private Set<Material> getMaterials(String string) {
        HashSet<Material> hashSet = new HashSet<Material>();
        for (String string2 : this.blocks.getStringList(string)) {
            try {
                hashSet.add(Material.valueOf((String)string2));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        return hashSet;
    }

    public Set<Material> getPassable() {
        return this.passable;
    }

    public Set<Material> getDoors() {
        return this.doors;
    }

    public Set<Material> getTrapdoors() {
        return this.trapdoors;
    }

    public Set<Material> getGates() {
        return this.gates;
    }

    public Set<Material> getFences() {
        return this.fences;
    }
}

