/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.commands;

import ivorius.ivtoolkit.blocks.BlockArea;
import ivorius.reccomplex.capability.SelectionOwner;
import ivorius.ivtoolkit.world.MockWorld;
import ivorius.reccomplex.commands.parameters.RCExpect;
import ivorius.reccomplex.commands.parameters.RCParameters;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ivorius.reccomplex.RCConfig;
import ivorius.reccomplex.utils.ServerTranslations;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by lukas on 09.06.14.
 */
public class CommandSelectNatural extends CommandVirtual
{
    @Override
    public String getName()
    {
        return RCConfig.commandPrefix + "natural";
    }

    @Override
    public String getUsage(ICommandSender var1)
    {
        return ServerTranslations.usage("commands.selectNatural.usage");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return RCExpect.startRC()
                .any("0", "1", "2")
                .get(server, sender, args, pos);
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    void execute(MockWorld world, ICommandSender commandSender, String[] args) throws CommandException
    {
        RCParameters parameters = RCParameters.of(args);

        SelectionOwner selectionOwner = RCCommands.getSelectionOwner(commandSender, null, true);
        RCCommands.assertSize(commandSender, selectionOwner);
        BlockArea area = selectionOwner.getSelection();

        double expandFloor = parameters.get().doubleAt(0).optional().orElse(1.);

        CommandSelectFloor.placeNaturalFloor(world, area, expandFloor);
        CommandSelectSpace.placeNaturalAir(world, area, 3, 3);
    }
}
