package io.github.sasuked.rankingnpcs.ranking.implementation;

import io.github.sasuked.rankingnpcs.ranking.AbstractRanking;
import io.github.sasuked.rankingnpcs.ranking.entry.RankingEntry;
import io.github.sasuked.rankingnpcs.utils.StringHelper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MoneyRanking extends AbstractRanking {
  
  private Economy economy;
  
  public MoneyRanking() {
    super("vault-money-ranking");
    
    this.setEnabled(setupEconomy());
  }
  
  @Override
  public void addEntryKey(String key) {
    if (economy == null || !isEnabled() || key == null) {
      return;
    }
    
    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(key);
    if (offlinePlayer == null) {
      return;
    }
    
    double money = economy.getBalance(offlinePlayer);
    if (money <= 0) {
      return;
    }
    
    this.addEntry(new RankingEntry(key, (long) money));
  }
  
  private boolean setupEconomy() {
    RegisteredServiceProvider<Economy> registration = Bukkit.getServicesManager().getRegistration(Economy.class);
    if (registration == null) {
      return false;
    }
    
    economy = registration.getProvider();
    return economy != null;
  }
  
  @Override
  public void update() {
    getEntryList().clear();
    
    for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
      if (offlinePlayer == null) {
        continue;
      }
      
      this.addEntryKey(offlinePlayer.getName());
    }
    
    super.update();
  }
  
  @Override
  public String formatPosition(int position) {
    return "§e§lPosition §e#" + position;
  }
  
  @Override
  public String formatKey(String key) {
    return ChatColor.GOLD.toString() + key;
  }
  
  @Override
  public String formatValue(long value) {
    return "§fBalance: §b" + StringHelper.format(value);
  }
}
