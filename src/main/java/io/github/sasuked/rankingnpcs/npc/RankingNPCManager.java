package io.github.sasuked.rankingnpcs.npc;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import io.github.sasuked.rankingnpcs.RankingNpcsPlugin;
import io.github.sasuked.rankingnpcs.utils.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RankingNPCManager {
  
  private final Map<Integer, RankingNPC> npcMap;
  private final YamlConfig config;
  
  public RankingNPCManager() {
    npcMap = new ConcurrentHashMap<>();
    config = new YamlConfig(RankingNpcsPlugin.getInstance(), "npcs.yml");
    
    this.load();
  }
  
  public void load() {
    ConfigurationSection npcs = config.getConfigurationSection("npcs");
    if (npcs == null) {
      return;
    }
    
    npcs.getKeys(false)
      .stream()
      .filter(this::isInt)
      .mapToInt(Integer::parseInt)
      .mapToObj(id -> RankingNPC.read(id,npcs))
      .filter(Objects::nonNull)
      .forEachOrdered(this::register);
      
    
    Bukkit.getConsoleSender().sendMessage("§a[RankingNPCs] Loaded " + npcMap.size() + " npcs.");
  }
  
  public void saveNpc(RankingNPC npc) {
    String path = "npcs." + npc.getNpcId();
    
    config.set(path + ".ranking-key", npc.getRankingKey());
    config.set(path + ".position", npc.getPosition());
    
    config.save();
  }
  
  public void delete(RankingNPC npc) {
    
    Hologram hologram = npc.getHologram();
    if (hologram != null) {
      hologram.delete();
    }
    
    npcMap.remove(npc.getNpcId());
    
    String path = "npcs." + npc.getNpcId();
    config.set(path, null);
    
    config.save();
  }
  
  public void updateAll() {
    npcMap.values().forEach(RankingNPC::update);
  }
  
  public void register(RankingNPC npc) {
    npcMap.put(npc.getNpcId(), npc);
  }
  
  public void unregister(RankingNPC npc) {
    npcMap.remove(npc.getNpcId());
  }
  
  public RankingNPC getNPC(int id) {
    return npcMap.get(id);
  }
  
  public Map<Integer, RankingNPC> getNpcMap() {
    return npcMap;
  }
  
  
  boolean isInt(String key) {
    try {
      Integer.parseInt(key);
    } catch (Exception e) {
      return false;
    }
    
    return true;
  }
}
