package objblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.model.IModelCustom;
import objblock.client.modelloader.MQO_MetasequoiaObject;

/*
 * TileEntityのクラスです。
 * TileEntityは、Tick毎に特殊な動作をしたり、複雑なモデルを持ったり、
 * NBTを使ってデータを格納したり、色々な用途に使えます。
 *
 * ただしこのクラス内で行われた処理やデータは基本的にサーバ側にしかないので、
 * 同期処理についてよく考えて実装する必要があります。
 */
public class OB_TileEntity extends TileEntity
{
	@SideOnly(Side.CLIENT)
	public IModelCustom model;

	public int getBlockMetadata()
	{
		if( this.blockMetadata == -1 )
		{
			this.blockMetadata = this.worldObj.getBlockMetadata( this.xCoord, this.yCoord, this.zCoord );
		}

		return this.blockMetadata;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		AxisAlignedBB bb = super.getRenderBoundingBox();
		OB_Block type = (OB_Block) getBlockType();

		if(type.model instanceof MQO_MetasequoiaObject)
		{
			MQO_MetasequoiaObject mo = (MQO_MetasequoiaObject) type.model;
			double size = Math.max( Math.abs( mo.max ), Math.abs( mo.min ) );
			return bb.expand( size, size, size );
		}

		return bb;
	}
}
