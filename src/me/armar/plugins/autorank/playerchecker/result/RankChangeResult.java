package me.armar.plugins.autorank.playerchecker.result;

import java.util.ArrayList;

import me.armar.plugins.autorank.api.events.PlayerPromoteEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RankChangeResult extends Result {

	String from = null;
	String to = null;
	String world = null;

	@Override
	public boolean setOptions(final String[] options) {
		//1 arg -> from group that the rank is being applied for to arg 0
		if (options.length == 1) {
			to = options[0].trim();
		}
		//2 args -> from arg 0 to arg 1
		if (options.length == 2) {
			from = options[0].trim();
			to = options[1].trim();
		}
		//3 args -> from arg 0 to arg 1 in world arg 2
		if (options.length == 3) {
			from = options[0].trim();
			to = options[1].trim();
			world = options[2].trim();
		}

		return to != null;
	}

	@Override
	public boolean applyResult(final Player player, final String group) {
		String oldrank = null;
		if (from == null) {
			oldrank = group;
		} else {
			oldrank = from;
		}
		if (world != null) {
			this.getAutorank().getLogger().info("Promote " + player.getName() + " on world "
					+ world + " from " + oldrank + " to " + to);
		} else {
			this.getAutorank().getLogger().info("Promote " + player.getName()
					+ " globally from " + oldrank + " to " + to);
		}

		// Call PlayerPromoteEvent

		// Create the event here
		final PlayerPromoteEvent event = new PlayerPromoteEvent(player, world,
				oldrank, to);
		// Call the event
		Bukkit.getServer().getPluginManager().callEvent(event);

		// Check if event is cancelled.
		if (event.isCancelled())
			return false;

		// When rank is changed: reset progress and update last known group
		getAutorank().getRequirementHandler().setPlayerProgress(
				player.getName(), new ArrayList<Integer>());

		getAutorank().getRequirementHandler().setLastKnownGroup(
				player.getName(), to);

		return this.getAutorank().getPermPlugHandler().getPermissionPlugin()
				.replaceGroup(player, world, oldrank, to);
	}

	@Override
	public boolean applyResult(final Player player) {
		return this.getAutorank().getPermPlugHandler().getPermissionPlugin()
				.replaceGroup(player, world, from, to);
	}

}
