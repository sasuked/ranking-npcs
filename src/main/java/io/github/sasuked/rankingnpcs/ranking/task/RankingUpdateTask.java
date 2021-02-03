package io.github.sasuked.rankingnpcs.ranking.task;

import io.github.sasuked.rankingnpcs.RankingNpcsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class RankingUpdateTask extends BukkitRunnable {
  
  private final RankingNpcsPlugin plugin;
  
  public RankingUpdateTask(RankingNpcsPlugin plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public void run() {
    Bukkit.getScheduler().runTask(plugin, () -> {
      plugin.getRankingManager().updateAll();
      plugin.getNpcManager().updateAll();
    });
  }
}
