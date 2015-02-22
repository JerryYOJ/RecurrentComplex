/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.gui.editstructure.gentypes;

import ivorius.ivtoolkit.gui.IntegerRange;
import ivorius.reccomplex.gui.editstructure.TableDataSourceBiomeGenList;
import ivorius.reccomplex.gui.editstructure.TableDataSourceDimensionGenList;
import ivorius.reccomplex.gui.table.*;
import ivorius.reccomplex.structures.generic.GenerationYSelector;
import ivorius.reccomplex.structures.generic.gentypes.NaturalGenerationInfo;
import ivorius.reccomplex.utils.IvTranslations;
import ivorius.reccomplex.worldgen.StructureSelector;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lukas on 07.10.14.
 */
public class TableDataSourceNaturalGenerationInfo extends TableDataSourceSegmented implements TableElementActionListener, TableElementPropertyListener
{
    private TableNavigator navigator;
    private TableDelegate tableDelegate;
    private NaturalGenerationInfo naturelGenerationInfo;

    public TableDataSourceNaturalGenerationInfo(TableNavigator navigator, TableDelegate tableDelegate, NaturalGenerationInfo naturelGenerationInfo)
    {
        this.navigator = navigator;
        this.tableDelegate = tableDelegate;
        this.naturelGenerationInfo = naturelGenerationInfo;
    }

    public static List<TableElementList.Option> allGenerationOptions()
    {
        List<TableElementList.Option> generationBases = new ArrayList<>();

        for (GenerationYSelector.SelectionMode selectionMode : GenerationYSelector.SelectionMode.values())
        {
            String transKeyBase = "structures.genY." + selectionMode.serializedName();
            generationBases.add(new TableElementList.Option(selectionMode.serializedName(),
                    I18n.format(transKeyBase), IvTranslations.formatLines(transKeyBase + ".tooltip")));
        }

        return generationBases;
    }

    public static List<TableElementList.Option> allGenerationCategories()
    {
        Set<String> categories = StructureSelector.allCategoryIDs();
        List<TableElementList.Option> generationBases = new ArrayList<>();

        for (String category : categories)
        {
            StructureSelector.Category categoryObj = StructureSelector.categoryForID(category);

            if (categoryObj.selectableInGUI())
            {
                String transKeyBase = "structures.category." + category;

                generationBases.add(new TableElementList.Option(category,
                        I18n.format(transKeyBase), IvTranslations.formatLines(transKeyBase + ".tooltip")));
            }
        }

        return generationBases;
    }

    @Override
    public int numberOfSegments()
    {
        return 3;
    }

    @Override
    public int sizeOfSegment(int segment)
    {
        switch (segment)
        {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
        }

        return 0;
    }

    @Override
    public TableElement elementForIndexInSegment(GuiTable table, int index, int segment)
    {
        if (segment == 0)
        {
            if (index == 0)
            {
                TableElementList element = new TableElementList("category", "Category", naturelGenerationInfo.generationCategory, allGenerationCategories());
                element.addPropertyListener(this);
                return element;
            }
        }
        else if (segment == 1)
        {
            if (index == 0)
            {
                TableElementList element = new TableElementList("ySelType", "Generation Base", naturelGenerationInfo.ySelector.selectionMode.serializedName(), allGenerationOptions());
                element.addPropertyListener(this);
                return element;
            }
            else if (index == 1)
            {
                TableElementIntegerRange element = new TableElementIntegerRange("ySelShift", "Y Shift", new IntegerRange(naturelGenerationInfo.ySelector.minY, naturelGenerationInfo.ySelector.maxY), -100, 100);
                element.addPropertyListener(this);
                return element;
            }
        }
        else if (segment == 2)
        {
            if (index == 0)
            {
                TableElementBoolean element = new TableElementBoolean("defaultWeight", "Use Default Weight", naturelGenerationInfo.hasDefaultWeight());
                element.addPropertyListener(this);
                return element;
            }
            else if (index == 1)
            {
                TableElementFloat element = new TableElementFloat("weight", "Weight", (float) naturelGenerationInfo.getActiveSpawnWeight(), 0, 10);
                element.setEnabled(!naturelGenerationInfo.hasDefaultWeight());
                element.addPropertyListener(this);
                return element;
            }
            else if (index == 2)
            {
                TableElementButton elementEditBiomes = new TableElementButton("editBiomes", "Biomes", new TableElementButton.Action("edit", "Edit"));
                elementEditBiomes.addListener(this);
                return elementEditBiomes;
            }
            else if (index == 3)
            {
                TableElementButton elementEditBiomes = new TableElementButton("editDimensions", "Dimensions", new TableElementButton.Action("edit", "Edit"));
                elementEditBiomes.addListener(this);
                return elementEditBiomes;
            }
        }

        return null;
    }

    @Override
    public void actionPerformed(TableElement element, String actionID)
    {
        if ("editBiomes".equals(element.getID()) && "edit".equals(actionID))
        {
            GuiTable editBiomesProperties = new GuiTable(tableDelegate, new TableDataSourceBiomeGenList(naturelGenerationInfo.biomeWeights, tableDelegate, navigator));
            navigator.pushTable(editBiomesProperties);
        }
        else if ("editDimensions".equals(element.getID()) && "edit".equals(actionID))
        {
            GuiTable editBiomesProperties = new GuiTable(tableDelegate, new TableDataSourceDimensionGenList(naturelGenerationInfo.dimensionWeights, tableDelegate, navigator));
            navigator.pushTable(editBiomesProperties);
        }
    }

    @Override
    public void valueChanged(TableElementPropertyDefault element)
    {
        if ("category".equals(element.getID()))
        {
            naturelGenerationInfo.generationCategory = (String) element.getPropertyValue();
        }
        else if ("weight".equals(element.getID()))
        {
            naturelGenerationInfo.setGenerationWeight((double) (Float) element.getPropertyValue());
        }
        else if ("defaultWeight".equals(element.getID()))
        {
            boolean useDefault = (boolean) element.getPropertyValue();
            naturelGenerationInfo.setGenerationWeight(useDefault ? null : naturelGenerationInfo.getActiveSpawnWeight());
            tableDelegate.reloadData();
        }
        else if ("ySelType".equals(element.getID()))
        {
            GenerationYSelector.SelectionMode selectionMode = GenerationYSelector.SelectionMode.selectionMode((String) element.getPropertyValue());
            naturelGenerationInfo.ySelector.selectionMode = selectionMode != null ? selectionMode : GenerationYSelector.SelectionMode.SURFACE;
        }
        else if ("ySelShift".equals(element.getID()))
        {
            IntegerRange range = ((IntegerRange) element.getPropertyValue());
            naturelGenerationInfo.ySelector.minY = range.getMin();
            naturelGenerationInfo.ySelector.maxY = range.getMax();
        }
    }
}