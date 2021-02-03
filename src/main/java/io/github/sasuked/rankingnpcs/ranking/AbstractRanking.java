package io.github.sasuked.rankingnpcs.ranking;

import io.github.sasuked.rankingnpcs.ranking.entry.RankingEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractRanking implements Ranking {
  
  private String key;
  private boolean enabled;
  private List<RankingEntry> entryList;
  
  public AbstractRanking(String key) {
    this.key = key;
    this.enabled = true;
    this.entryList = Collections.synchronizedList(new ArrayList<>());
  }
  
  @Override
  public String getKey() {
    return key;
  }
  
  @Override
  public boolean isEnabled() {
    return enabled;
  }
  
  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  @Override
  public List<RankingEntry> asList() {
    return entryList;
  }
  
  @Override
  public int getPosition(String key) {
    for (int i = 0, pairListSize = entryList.size(); i < pairListSize; i++) {
      RankingEntry rankingEntry = entryList.get(i);
      if (rankingEntry != null && rankingEntry.getKey().equalsIgnoreCase(key)) {
        return i;
      }
    }
    
    return -1;
  }
  
  @Override
  public RankingEntry getEntry(int position) {
    try {
      return entryList.get(position);
    } catch (Exception e) {
      return null;
    }
  }
  
  @Override
  public void update() {
    entryList.sort(Comparator.comparingDouble(RankingEntry::getValue).reversed());
  }
  
  protected List<RankingEntry> getEntryList() {
    return entryList;
  }
  
  protected boolean addEntry(RankingEntry entry){
    return entryList.add(entry);
  }
}
