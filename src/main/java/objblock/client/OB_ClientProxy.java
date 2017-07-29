package objblock.client;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModCandidate;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import objblock.OB_CommonProxy;
import objblock.OB_Block;
import objblock.OB_Core;
import objblock.OB_TileEntity;
import objblock.client.modelloader.MQO_ModelLoader;

public class OB_ClientProxy extends OB_CommonProxy
{
	/** The file locations of the content packs, used for loading */
	public List<File> contentPacks;

	@Override
	public void registerRenderer()
	{
		AdvancedModelLoader.registerModelHandler( new MQO_ModelLoader() );
		ClientRegistry.bindTileEntitySpecialRenderer( OB_TileEntity.class, new OB_Renderer() );
	}

	@Override
	public void registerItemRenderer()
	{
		for(OB_Block block : OB_Core.objBlocks)
		{
			MinecraftForgeClient.registerItemRenderer( Item.getItemFromBlock( block ), new OB_ItemRender( block ) );
		}
	}

	@Override
	public void addModel(OB_Block block, String name)
	{
		block.model = AdvancedModelLoader.loadModel( new ResourceLocation( OB_Core.MOD_ID, "models/" + name + ".mqo" ) );
		if( block.model == null )
		{
			block.model = AdvancedModelLoader.loadModel( new ResourceLocation( OB_Core.MOD_ID, "models/" + name + ".obj" ) );
		}
	}

	/** This method grabs all the content packs and puts them in a list. The client side part registers them as FMLModContainers which adds their resources to the game after a refresh */
	@Override
	public List<File> getContentList(Method method, ClassLoader classloader)
	{
		contentPacks = new ArrayList<File>();
		for( File file : OB_Core.resourceDir.listFiles() )
		{
			if( file.isDirectory() || zipJar.matcher( file.getName() ).matches() )
			{
				try
				{
					method.invoke( classloader, file.toURI().toURL() );

					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put( "modid", OB_Core.MOD_ID );
					map.put( "name", "Obj block : " + file.getName() );
					map.put( "version", "1.0" );
					FMLModContainer container = new FMLModContainer( "objblock.ObjBlockCore", new ModCandidate( file, file, file.isDirectory() ? ContainerType.DIR : ContainerType.JAR ), map );
					container.bindMetadata( MetadataCollection.from( null, "" ) );
					FMLClientHandler.instance().addModAsResource( container );
				}
				catch( Exception e )
				{
					OB_Core.log( "Failed to load images for content pack : " + file.getName() );
					e.printStackTrace();
				}
				// Add the directory to the content pack list
				OB_Core.log( "Loaded content pack : " + file.getName() );
				contentPacks.add( file );
			}
		}

		OB_Core.log( "Loaded textures and models." );
		return contentPacks;
	}

	@Override
	public File getRootDir()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public String getSideString()
	{
		return "Client";
	}
}
