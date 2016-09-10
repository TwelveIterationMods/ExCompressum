package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandExCompressum extends CommandBase {
	@Override
	public String getCommandName() {
		return "excompressum";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/excompressum reload";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			throw new WrongUsageException(getCommandUsage(sender));
		}
		if(args[0].equals("reload")) {
			ChickenStickRegistry.INSTANCE.load(ExCompressum.configDir);
			CompressedHammerRegistry.INSTANCE.load(ExCompressum.configDir);
			HeavySieveRegistry.INSTANCE.load(ExCompressum.configDir);
			WoodenCrucibleRegistry.INSTANCE.load(ExCompressum.configDir);
			sender.addChatMessage(new TextComponentTranslation("commands.excompressum:reload.success"));
		} else {
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}
}
