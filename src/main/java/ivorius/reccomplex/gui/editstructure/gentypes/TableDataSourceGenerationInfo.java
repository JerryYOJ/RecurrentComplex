/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.gui.editstructure.gentypes;

import ivorius.ivtoolkit.tools.IvTranslations;
import ivorius.reccomplex.gui.GuiValidityStateIndicator;
import ivorius.reccomplex.gui.table.*;
import ivorius.reccomplex.gui.table.cell.TableCellButton;
import ivorius.reccomplex.gui.table.cell.TableCellMulti;
import ivorius.reccomplex.gui.table.cell.TableCellString;
import ivorius.reccomplex.gui.table.cell.TableElementCell;
import ivorius.reccomplex.gui.table.datasource.TableDataSourceSegmented;
import ivorius.reccomplex.world.gen.feature.structure.StructureInfos;
import ivorius.reccomplex.world.gen.feature.structure.generic.gentypes.StructureGenerationInfo;

/**
 * Created by lukas on 26.03.15.
 */
public class TableDataSourceGenerationInfo extends TableDataSourceSegmented
{
    public StructureGenerationInfo genInfo;

    public TableDelegate delegate;

    public TableDataSourceGenerationInfo(StructureGenerationInfo genInfo, TableNavigator navigator, TableDelegate delegate)
    {
        this.genInfo = genInfo;
        this.delegate = delegate;
    }

    @Override
    public int numberOfSegments()
    {
        return 1;
    }

    @Override
    public int sizeOfSegment(int segment)
    {
        return segment == 0 ? 1 : super.sizeOfSegment(segment);
    }

    @Override
    public TableElement elementForIndexInSegment(GuiTable table, int index, int segment)
    {
        if (segment == 0)
        {
            TableCellString idCell = new TableCellString("genInfoID", genInfo.id());
            idCell.setShowsValidityState(true);
            idCell.setValidityState(currentIDState());
            idCell.addPropertyConsumer(val ->
            {
                genInfo.setID(val);
                idCell.setValidityState(currentIDState());
            });

            TableCellButton randomizeCell = new TableCellButton(null, null, IvTranslations.get("reccomplex.gui.randomize.short"), IvTranslations.getLines("reccomplex.gui.randomize"));
            randomizeCell.addAction(() -> {
                genInfo.setID(StructureGenerationInfo.randomID(genInfo.getClass()));
                delegate.reloadData();
            });

            TableCellMulti cell = new TableCellMulti(idCell, randomizeCell);
            cell.setSize(1, 0.1f);
            return new TableElementCell(IvTranslations.get("reccomplex.structure.generation.id"), cell)
                    .withTitleTooltip(IvTranslations.formatLines("reccomplex.structure.generation.id.tooltip"));
        }

        return super.elementForIndexInSegment(table, index, segment);
    }

    protected GuiValidityStateIndicator.State currentIDState()
    {
        return StructureInfos.isSimpleIDState(genInfo.id());
    }
}
