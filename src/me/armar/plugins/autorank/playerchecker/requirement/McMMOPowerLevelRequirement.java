package me.armar.plugins.autorank.playerchecker.requirement;

import java.util.ArrayList;
import java.util.List;

import me.armar.plugins.autorank.hooks.DependencyManager.dependency;
import me.armar.plugins.autorank.hooks.mcmmoapi.McMMOHandler;
import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.autorank.playerchecker.result.Result;

import org.bukkit.entity.Player;

public class McMMOPowerLevelRequirement extends Requirement {

	private int powerLevel = 0;
	private boolean optional = false;
	private boolean autoComplete = false;
	private int reqId;
	List<Result> results = new ArrayList<Result>();

	public McMMOPowerLevelRequirement() {
		super();
	}

	@Override
	public boolean setOptions(final String[] options, final boolean optional,
			final List<Result> results, final boolean autoComplete,
			final int reqId) {
		this.optional = optional;
		this.results = results;
		this.autoComplete = autoComplete;
		this.reqId = reqId;

		if (options.length > 0) {
			powerLevel = Integer.parseInt(options[0]);
		}
		
		return true;
	}

	@Override
	public boolean meetsRequirement(final Player player) {
		// TODO Auto-generated method stub
		if (isCompleted(getReqId(), player.getName())) {
			return true;
		}

		McMMOHandler handler = (McMMOHandler) this.getAutorank().getDependencyManager().getDependency(dependency.MCMMO);
		
		int level = handler.getPowerLevel(player);
		
		return level > 0 && level >= powerLevel;
	}

	@Override
	public String getDescription() {
		
		return Lang.MCMMO_POWER_LEVEL_REQUIREMENT
				.getConfigValue(new String[] {powerLevel + ""});
	}

	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public List<Result> getResults() {
		return results;
	}

	@Override
	public String getProgress(final Player player) {
		String progress = "";
		int level = ((McMMOHandler) this.getAutorank().getDependencyManager().getDependency(dependency.MCMMO)).getPowerLevel(player);

		progress = progress.concat(level + "/" + powerLevel);
		return progress;
	}

	@Override
	public boolean useAutoCompletion() {
		return autoComplete;
	}

	@Override
	public int getReqId() {
		return reqId;
	}
}
