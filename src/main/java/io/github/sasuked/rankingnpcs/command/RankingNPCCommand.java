package io.github.sasuked.rankingnpcs.command;

import io.github.sasuked.rankingnpcs.RankingNpcsPlugin;
import io.github.sasuked.rankingnpcs.npc.RankingNPC;
import io.github.sasuked.rankingnpcs.npc.RankingNPCManager;
import io.github.sasuked.rankingnpcs.ranking.Ranking;
import io.github.sasuked.rankingnpcs.utils.StringHelper;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class RankingNPCCommand extends Command {
  
  private final RankingNpcsPlugin plugin = RankingNpcsPlugin.getInstance();
  
  public RankingNPCCommand() {
    super("rankingnpc");
    
    setAliases(Arrays.asList(
      "/rankingnpcs",
      "/ranknpc"
    ));
  }
  
  @Override
  public boolean execute(CommandSender commandSender, String s, String[] args) {
    if (!commandSender.hasPermission("rankingnpcs.use")) {
      return response(commandSender, "§cVocê não tem permissões para este comando.");
    }
    
    
    if (args.length == 0) {
      return response(commandSender,
        "",
        " §eRankingNPCs Commands",
        " ",
        " §a/rankingnpcs create [ranking] [position] - cria um npc com a posição e rank definidos.",
        " §a/rankingnpcs delete - deleta um npc do ranking.",
        " §a/rankingnpcs update - atualiza os npcs.",
        ""
      );
    }
    
    if (args[0].equalsIgnoreCase("create")) {
      return createArgument(commandSender, args);
    }
    if (args[0].equalsIgnoreCase("delete")) {
      NPC selectedNpc = CitizensAPI.getDefaultNPCSelector().getSelected(commandSender);
      if (selectedNpc == null) {
        return response(commandSender, "§cSelecione um npc para usar este comando.");
      }
      
      int npcId = selectedNpc.getId();
      
      RankingNPCManager npcManager = plugin.getNpcManager();
      
      RankingNPC npc = npcManager.getNPC(npcId);
      if (npc == null) {
        return response(commandSender, "§cEste npc não existe!");
      }
      
      npcManager.delete(npc);
      return response(commandSender, "§cNpc deletado.");
    }
    
    return false;
  }
  
  private boolean createArgument(CommandSender commandSender, String[] args) {
    if (args.length == 1) {
      return response(commandSender, "§aEspecifique um ranking para o npc.");
    }
    
    NPC selectedNpc = CitizensAPI.getDefaultNPCSelector().getSelected(commandSender);
    if (selectedNpc == null) {
      return response(commandSender, "§cSelecione um npc para utilizar este comando.");
    }
    
    Ranking ranking = plugin.getRankingManager().getRankingFromKey(args[1]);
    if (ranking == null) {
      return response(commandSender,
        "§cEste ranking não existe.",
        "",
        " §cLista de rankings válidos: " + plugin.getRankingManager().getRankingListLabel(),
        ""
      );
    }
    
    if (args.length == 2 || !StringHelper.isInt(args[2])) {
      return response(commandSender, "§cEspecifique uma posição válida.");
    }
    
    int position = Integer.parseInt(args[2]);
    
    RankingNPC npc = new RankingNPC(selectedNpc.getId(), ranking.getKey(), position);
    
    RankingNPCManager npcManager = plugin.getNpcManager();
    npcManager.register(npc);
    npcManager.saveNpc(npc);
    
    return response(commandSender, "§aNPC criado!");
  }
  
  
  private boolean response(CommandSender sender, String... msg) {
    sender.sendMessage(msg);
    return false;
  }
}

