package io.github.sasuked.rankingnpcs.npc;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import io.github.sasuked.rankingnpcs.RankingNpcsPlugin;
import io.github.sasuked.rankingnpcs.ranking.Ranking;
import io.github.sasuked.rankingnpcs.ranking.entry.RankingEntry;
import lombok.Data;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import static org.apache.commons.lang.StringUtils.containsIgnoreCase;

@Data
public class RankingNPC {
  
  
  private int npcId;
  private String rankingKey;
  private int position;
  
  private Hologram hologram;
  
  public RankingNPC(int npcId, String rankingKey, int position) {
    this.npcId = npcId;
    this.rankingKey = rankingKey;
    this.position = position;
  }
  
  public static RankingNPC read(int npcId, ConfigurationSection rootSection) {
    ConfigurationSection npcSection = rootSection.getConfigurationSection(String.valueOf(npcId));
    if (npcSection == null) {
      return null;
    }
    
    String rankingKey = npcSection.getString("ranking-key");
    int position = npcSection.getInt("position");
    
    return new RankingNPC(npcId, rankingKey, position);
  }
  
  public void update() {
    NPC npc = CitizensAPI.getNPCRegistry().getById(npcId);
    if (npc == null) {
      return;
    }
    
    RankingNpcsPlugin plugin = RankingNpcsPlugin.getInstance();
    if (!plugin.isEnabled()) {
      return;
    }
    
    Ranking ranking = plugin.getRankingManager().getRankingFromKey(rankingKey);
    if (ranking == null || !ranking.isEnabled()) {
      return;
    }
    
    RankingEntry entry = ranking.getEntry(position - 1);
    if (entry == null) {
      return;
    }
    
    String entryKey = entry.getKey();
    if (!ChatColor.stripColor(npc.getFullName()).equalsIgnoreCase(entryKey)) {
      npc.setName(ranking.formatKey(entryKey));
    }
    
    SkinTrait skinTrait = npc.getTraitNullable(SkinTrait.class);
    if (skinTrait == null) {
      return;
    }
    
    skinTrait.setShouldUpdateSkins(true);
    
    this.updateHologram(npc.getStoredLocation(), position, entry);
    
    String skinName = skinTrait.getSkinName();
    if (skinName != null && !containsIgnoreCase(skinName, entryKey)) {
      skinTrait.setSkinName(entryKey, false);
    }
  }
  
  private void updateHologram(Location location, int position, RankingEntry entry) {
    Ranking ranking = this.getRanking();
    if (ranking == null) {
      return;
    }
    
    String formattedPosition = ranking.formatPosition(position);
    String formattedValue = ranking.formatValue(entry.getValue());
    
    Location hologramLocation = location.add(0, 3, 0);
    if (hologram == null) {
      hologram = HologramsAPI.createHologram(RankingNpcsPlugin.getInstance(), hologramLocation);
      hologram.appendTextLine(formattedPosition);
      hologram.appendTextLine(formattedValue);
    } else {
      HologramLine keyLine = hologram.getLine(0);
      if (keyLine instanceof TextLine) {
        ((TextLine) keyLine).setText(formattedPosition);
      }
      HologramLine valueLine = hologram.getLine(1);
      if (valueLine instanceof TextLine) {
        ((TextLine) valueLine).setText(formattedValue);
      }
    }
    
    hologram.teleport(hologramLocation);
  }
  
  public Ranking getRanking() {
    return RankingNpcsPlugin.getInstance()
      .getRankingManager()
      .getRankingFromKey(rankingKey.toLowerCase());
  }
}
