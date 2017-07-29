package objblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class OB_CreativeTabs extends CreativeTabs
{

	public OB_CreativeTabs(String lable)
	{
		super( lable );
	}

	@Override
    @SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Items.apple;
	}

    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return this.getTabLabel();
    }
}
