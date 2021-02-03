package io.github.sasuked.rankingnpcs;

import io.github.sasuked.rankingnpcs.command.RankingNPCCommand;
import io.github.sasuked.rankingnpcs.npc.RankingNPCManager;
import io.github.sasuked.rankingnpcs.ranking.RankingManager;
import io.github.sasuked.rankingnpcs.ranking.implementation.MoneyRanking;
import io.github.sasuked.rankingnpcs.ranking.task.RankingUpdateTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public final class RankingNpcsPlugin extends JavaPlugin {
  
  private RankingManager rankingManager;
  private RankingNPCManager npcManager;
  
  @Override
  public void onEnable() {
    PluginManager pluginManager = Bukkit.getPluginManager();
    if (pluginManager.getPlugin("Citizens") == null) {
      getLogger().warning("Citizens not found, disabling plugin.");
      
      pluginManager.disablePlugin(this);
      return;
    }
    
    
    rankingManager = new RankingManager(this);
    
    if (pluginManager.getPlugin("Vault") != null) {
      rankingManager.registerRanking(new MoneyRanking());
    }
    
    
    this.npcManager = new RankingNPCManager();
    
    
    RankingUpdateTask task = new RankingUpdateTask(this);
    task.runTaskTimerAsynchronously(this, 0, 180);
    
    registerCommand(new RankingNPCCommand());
  }
  
  @Override
  public void onDisable() {
  
  }
  
  public static RankingNpcsPlugin getInstance() {
    return getPlugin(RankingNpcsPlugin.class);
  }
  
  private void registerCommand(Command... commands) {
    ((CraftServer) getServer()).getCommandMap()
      .registerAll("rankingnpcs", Arrays.asList(commands));
  }
}
