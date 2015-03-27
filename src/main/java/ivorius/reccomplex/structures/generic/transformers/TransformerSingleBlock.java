/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.structures.generic.transformers;

import ivorius.ivtoolkit.blocks.BlockCoord;
import ivorius.ivtoolkit.blocks.IvBlockCollection;
import ivorius.ivtoolkit.tools.IvWorldData;
import ivorius.reccomplex.structures.StructureSpawnContext;
import net.minecraft.block.Block;

import java.util.List;

/**
 * Created by lukas on 17.09.14.
 */
public abstract class TransformerSingleBlock implements Transformer
{
    @Override
    public boolean skipGeneration(Block block, int metadata)
    {
        return matches(block, metadata);
    }

    @Override
    public void transform(Phase phase, StructureSpawnContext context, IvWorldData worldData, List<Transformer> transformerList)
    {
        // TODO Fix for partial generation

        IvBlockCollection blockCollection = worldData.blockCollection;
        int[] areaSize = new int[]{blockCollection.width, blockCollection.height, blockCollection.length};
        BlockCoord lowerCoord = context.lowerCoord();

        for (BlockCoord sourceCoord : blockCollection)
        {
            BlockCoord worldCoord = context.transform.apply(sourceCoord, areaSize).add(lowerCoord);

            if (context.includes(worldCoord))
            {
                Block block = blockCollection.getBlock(sourceCoord);
                int meta = blockCollection.getMetadata(sourceCoord);

                if (matches(block, meta))
                    transformBlock(context, Phase.BEFORE, worldCoord, block, meta);
            }
        }
    }

    public abstract boolean matches(Block block, int metadata);

    public abstract void transformBlock(StructureSpawnContext context, Phase phase, BlockCoord coord, Block sourceBlock, int sourceMetadata);
}