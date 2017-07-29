package objblock.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import objblock.OB_Block;

public class OB_Renderer extends TileEntitySpecialRenderer
{
	public OB_Renderer()
	{
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double posX, double posY, double posZ, float var8)
	{
		if( tile.getBlockType() instanceof OB_Block == false )
			return;
		OB_Block block = (OB_Block) tile.getBlockType();
		if( block.model == null )
			return;

		GL11.glPushMatrix();

		GL11.glEnable(GL11.GL_CULL_FACE);

		GL11.glTranslated(posX + 0.5, posY, posZ + 0.5);

		float yaw = -tile.getBlockMetadata() * 45 + 180;
		GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();

		// カラー
		GL11.glColor4f(block.colorRed, block.colorGreen, block.colorBlue, block.colorAlpha);

		GL11.glEnable(GL11.GL_BLEND);
		int srcBlend = GL11.glGetInteger(GL11.GL_BLEND_SRC);
		int dstBlend = GL11.glGetInteger(GL11.GL_BLEND_DST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glShadeModel(GL11.GL_SMOOTH); // スムースシェーディング

		// テクスチャ
		this.bindTexture(block.texture);
		block.model.renderAll();

		GL11.glBlendFunc(srcBlend, dstBlend);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_FLAT); // フラットシェーディング

		GL11.glPopMatrix();
	}
}
