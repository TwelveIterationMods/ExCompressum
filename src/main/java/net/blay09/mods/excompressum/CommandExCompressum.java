package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.compat.ExCompressumReloadEvent;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.List;

public class CommandExCompressum extends CommandBase {
	@Override
	public String getName() {
		return "excompressum";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/excompressum reload";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			throw new WrongUsageException(getUsage(sender));
		}
		if(args[0].equals("reload")) {
			AbstractRegistry.registryErrors.clear();
			ChickenStickRegistry.INSTANCE.load(ExCompressum.configDir);
			CompressedHammerRegistry.INSTANCE.load(ExCompressum.configDir);
			HeavySieveRegistry.INSTANCE.load(ExCompressum.configDir);
			WoodenCrucibleRegistry.INSTANCE.load(ExCompressum.configDir);
			sender.sendMessage(new TextComponentTranslation("commands.excompressum:reload.success"));
			if(AbstractRegistry.registryErrors.size() > 0) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "There were errors loading the Ex Compressum registries:"));
				TextFormatting lastFormatting = TextFormatting.WHITE;
				for(String error : AbstractRegistry.registryErrors) {
					sender.sendMessage(new TextComponentString(lastFormatting + "* " + error));
					lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
				}
			}
			MinecraftForge.EVENT_BUS.post(new ExCompressumReloadEvent());
		} else {
			throw new WrongUsageException(getUsage(sender));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return getListOfStringsMatchingLastWord(args, "reload");
	}
}
