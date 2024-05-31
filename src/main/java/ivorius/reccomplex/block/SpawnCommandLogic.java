/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.block;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.*;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.*;
import ivorius.reccomplex.RCConfig;
import ivorius.reccomplex.utils.accessor.RCAccessorCommandBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.SimpleDateFormat;

/**
 * Created by lukas on 11.02.15.
 */
public abstract class SpawnCommandLogic extends CommandBlockBaseLogic
{
    /** The formatting for the timestamp on commands run. */
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private final CommandResultStats resultStats = new CommandResultStats();

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(this.getName());
    }

    @Override
    public void sendMessage(ITextComponent component)
    {
    }

    @Override
    public boolean sendCommandFeedback()
    {
        MinecraftServer minecraftserver = this.getServer();
        return minecraftserver == null || !minecraftserver.isAnvilFileSet() || minecraftserver.getWorld(0).getGameRules().getBoolean("commandBlockOutput");
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        this.resultStats.setCommandStatForSender(this.getServer(), this, type, amount);
    }

    public abstract void updateCommand();

    @SideOnly(Side.CLIENT)
    public abstract int getCommandBlockType();

    @SideOnly(Side.CLIENT)
    public abstract void fillInInfo(ByteBuf buf);

    public CommandResultStats getCommandResultStats()
    {
        return this.resultStats;
    }
}