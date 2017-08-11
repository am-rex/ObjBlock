package objblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModelCustom;

public class OB_Block extends BlockContainer implements ITileEntityProvider
{
	@SideOnly(Side.CLIENT)
	public IModelCustom model;
	public ResourceLocation texture;
	public String activatedSound = "";
	public float activatedSoundVolume = 1;
	public float activatedSoundPitch = 1;
	public float colorAlpha = 1;
	public float colorRed = 1;
	public float colorGreen = 1;
	public float colorBlue = 1;
	public float collidedDamage = 0;
	public boolean ladder;
	public float renderDist = 64;

	public OB_Block(Material mat)
	{
		super( mat );
	}

	/*
	 * ブロックの右クリックメソッド。
	 * TileEntityにString（文字列）でプレイヤー名を渡したり、
	 * 格納されたプレイヤー名を元にアクションを行わせる部分です。
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if( !world.isRemote )
		{
			if( !this.activatedSound.isEmpty() )
			{
				world.playSoundEffect( x + 0.5, y + 0.5, z + 0.5, OB_Core.MOD_ID + ":" + this.activatedSound, this.activatedSoundVolume, this.activatedSoundPitch );
			}
			else
			{
			//	int yaw = world.getBlockMetadata( x, y, z );

			//	world.setBlock( x, y, z, this, yaw, 2 );
			//	world.setBlockMetadataWithNotify( x, y, z, yaw, 2 );
			}
		}
		return true;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		if( this.ladder )
		{
			setBlockBoundsBasedOnState( world, x, y, z );
		}
		else if( this.collidedDamage != 0 )
		{
			float f = 0.0625F;
			return AxisAlignedBB.getBoundingBox(
				(double) ((float) x + f),
				(double) y,
				(double) ((float) z + f),
				(double) ((float) (x + 1) - f),
				(double) ((float) (y + 1) - f),
				(double) ((float) (z + 1) - f) );
		}
		return super.getCollisionBoundingBoxFromPool( world, x, y, z );
	}

	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int x, int y, int z)
	{
		if( this.ladder )
		{
			float f = 0.2F;
			switch (p_149719_1_.getBlockMetadata( x, y, z ))
			{
			case 0:
				this.setBlockBounds( 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f );
				break;

			case 2:
				this.setBlockBounds( 1.0F - f, 0.0F, 0F, 1, 1.0F, 1.0F );
				break;

			case 4:
				this.setBlockBounds( 0, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F );
				break;

			case 6:
				this.setBlockBounds( 0.0F, 0.0F, 0.0F, f, 1.0F, 1 );
				break;

			default:
				this.setBlockBounds( 0.0F, 0.0F, 0.0F, 1, 1.0F, 1.0F );
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
	{
		this.setBlockBoundsBasedOnState( p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_ );
		return super.getSelectedBoundingBoxFromPool( p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_ );
	}

	/*
	 * ブロックの設置時に生成されるTileEntityです。
	 */
	public TileEntity createNewTileEntity(World world, int a)
	{
		OB_TileEntity te = new OB_TileEntity();
		if( world.isRemote )
		{
			te.model = this.model;
		}
		return te;
	}

	public void onEntityCollidedWithBlock(World world, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity entity)
	{
		if( !world.isRemote )
		{
			if( this.collidedDamage > 0 )
			{
				entity.attackEntityFrom( DamageSource.generic, this.collidedDamage );
			}
			else if( this.collidedDamage < 0 && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase )
			{
				((EntityLivingBase) entity).heal( -this.collidedDamage / 2 );
			}
		}
	}

	@Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity)
	{
		return this.ladder;
	}

	// 地面に設置時、接地面が透過しないように
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
	{
		return true;
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	// 殴って壊せる＆アイテムドロップ
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		return true;
	}

	// ブロックの描画をしない モデルのみ
	public boolean canRenderInPass(int pass)
	{
		return false;
	}

	// ピストンで移動できない
	public int getMobilityFlag()
	{
		return 1;
	}

	@Override
	public void onBlockPlacedBy(World world, int par2, int par3, int par4, EntityLivingBase entity, ItemStack itemStack)
	{
		float pyaw = entity.rotationYaw + 22.5F + 180;
		while( pyaw >= 360 )
			pyaw -= 360;
		while( pyaw < 0 )
			pyaw += 360;
		int yaw = (int) (pyaw / 45);
		OB_Core.log( "onBlockPlacedBy yaw = %d", yaw );
		world.setBlockMetadataWithNotify( par2, par3, par4, yaw, 2 );
	}

	// 周囲の光源の強さに影響される
	public boolean getUseNeighborBrightness()
	{
		return true;
	}
}
