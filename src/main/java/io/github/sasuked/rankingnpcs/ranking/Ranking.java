package io.github.sasuked.rankingnpcs.ranking;

import io.github.sasuked.rankingnpcs.ranking.entry.RankingEntry;

import java.util.List;

public interface Ranking {
  
  String getKey();
  
  boolean isEnabled();
  
  void setEnabled(boolean enabled);
  
  int getPosition(String key);
  
  void addEntryKey(String key);
  
  List<RankingEntry> asList();
  
  RankingEntry getEntry(int position);
  
  String formatPosition(int position);
  
  String formatKey(String key);
  
  String formatValue(long value);
  
  void update();
}
