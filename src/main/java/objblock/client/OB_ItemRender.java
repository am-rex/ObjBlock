package objblock.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import objblock.OB_Block;
import objblock.client.modelloader.MQO_MetasequoiaObject;

public class OB_ItemRender implements IItemRenderer
{
	final OB_Block block;

	public OB_ItemRender(OB_Block block)
	{
		this.block = block;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch (type)
		{
		case ENTITY:
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case INVENTORY:
			return true;

		default:
			break;
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		GL11.glPushMatrix();

		GL11.glShadeModel(GL11.GL_SMOOTH); // スムースシェーディング

		bindTexture(block.texture);

		GL11.glEnable( GL12.GL_RESCALE_NORMAL );
		//		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		MQO_MetasequoiaObject mo = this.block.model instanceof MQO_MetasequoiaObject? (MQO_MetasequoiaObject)this.block.model : null;

		switch (type)
		{
		case ENTITY:
			GL11.glTranslatef( 0, 0.5F, 0 );
			if(mo != null)
			{
				GL11.glScalef( 3.0f / mo.max, 3.0f / mo.max, 3.0f / mo.max );
			}
			break;

		case INVENTORY:
			final float INV_SIZE = 0.75f;
			GL11.glTranslatef( 0, -0.5F, 0 );
			if(mo != null)
			{
				GL11.glScalef( 1.5f / mo.max, 1.5f / mo.max, 1.5f / mo.max );
			}
			else
			{
				GL11.glScalef( INV_SIZE, INV_SIZE, INV_SIZE );
			}
			break;

		case EQUIPPED:
			GL11.glRotatef( 270, 0, 1, 0 );
			GL11.glTranslatef( 0, 0, 0.5f );
			if(mo != null)
			{
				GL11.glScalef( 3.0f / mo.max, 3.0f / mo.max, 3.0f / mo.max );
			}
			break;

		case EQUIPPED_FIRST_PERSON:
			GL11.glRotatef( 270, 0, 1, 0 );
			GL11.glTranslatef( -0.4F, 0.75F, -0.2f );
			if(mo != null)
			{
				GL11.glScalef( 1.0f / mo.max, 1.0f / mo.max, 1.0f / mo.max );
			}
			GL11.glRotatef( 90, 0, -1, 0 );
			break;

		default:
			break;
		}

		block.model.renderAll();
		//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		GL11.glShadeModel(GL11.GL_FLAT); // フラットシェーディング

		GL11.glPopMatrix();

		GL11.glColor4f( 1, 1, 1, 1 );
		GL11.glEnable( GL11.GL_BLEND );
	}

	private void bindTexture(ResourceLocation texture)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture( texture );
	}
}
