package io.github.sasuked.rankingnpcs.ranking;

import io.github.sasuked.rankingnpcs.RankingNpcsPlugin;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RankingManager {
  
  private final RankingNpcsPlugin plugin;
  private final Map<String, Ranking> rankingMap;
  
  public RankingManager(RankingNpcsPlugin plugin) {
    this.plugin = plugin;
    this.rankingMap = new ConcurrentHashMap<>();
  }
  
  public void updateAll() {
    for (Ranking ranking : this.getRankings()) {
      if (ranking != null && ranking.isEnabled()) {
        ranking.update();
      }
    }
  }
  
  public void registerRanking(Ranking ranking) {
    rankingMap.put(ranking.getKey().toLowerCase(), ranking);
  }
  
  public Ranking getRankingFromKey(String key) {
    return rankingMap.get(key.toLowerCase());
  }
  
  public Ranking unregisterRanking(String key) {
    return rankingMap.remove(key.toLowerCase());
  }
  
  public Collection<Ranking> getRankings() {
    return rankingMap.values();
  }
  
  public Map<String, Ranking> getRankingMap() {
    return rankingMap;
  }
  
  public RankingNpcsPlugin getPlugin() {
    return plugin;
  }
  
  public String getRankingListLabel() {
    return rankingMap.values()
      .stream()
      .map(Ranking::getKey)
      .collect(Collectors.joining(", "));
  }
}
