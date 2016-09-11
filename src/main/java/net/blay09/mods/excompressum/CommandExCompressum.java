package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

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
			AbstractRegistry.registryErrors.clear();
			ChickenStickRegistry.INSTANCE.load(ExCompressum.configDir);
			CompressedHammerRegistry.INSTANCE.load(ExCompressum.configDir);
			HeavySieveRegistry.INSTANCE.load(ExCompressum.configDir);
			WoodenCrucibleRegistry.INSTANCE.load(ExCompressum.configDir);
			sender.addChatMessage(new TextComponentTranslation("commands.excompressum:reload.success"));
			if(AbstractRegistry.registryErrors.size() > 0) {
				sender.addChatMessage(new TextComponentString(TextFormatting.RED + "There were errors loading the Ex Compressum registries:"));
				TextFormatting lastFormatting = TextFormatting.WHITE;
				for(String error : AbstractRegistry.registryErrors) {
					sender.addChatMessage(new TextComponentString(lastFormatting + "* " + error));
					lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
				}
			}
		} else {
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}
}
