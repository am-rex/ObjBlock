package objblock;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

@Mod(modid = OB_Core.MOD_ID, name = "objblock", version = "1.0")
public class OB_Core
{
	public static final String MOD_ID = "objblock";

	@SidedProxy(clientSide = "objblock.client.OB_ClientProxy", serverSide = "objblock.OB_CommonProxy")
	public static OB_CommonProxy proxy;

	public static List<OB_Block> objBlocks = new ArrayList<OB_Block>();

	public static File resourceDir;

	List<File> contentPacks = new ArrayList<File>();
	HashMap<String, Material> materialMap = new HashMap<String, Material>();

	public static OB_CreativeTabs creativeTabs;

	public static void log(String format, Object... param)
	{
		System.out.printf( "[" + MOD_ID + "][" + proxy.getSideString() + "]" + format + "\n", param );
	}

	@EventHandler
	public void modConstruction(FMLConstructionEvent event)
	{
		resourceDir = new File( proxy.getRootDir(), "/ObjBlock/" );
		log( resourceDir.getAbsolutePath() );

		if( !resourceDir.exists() )
		{
			log( "ObjBlock folder not found. Creating empty folder." );
			log( "You should get some content packs and put them in the ObjBlock folder." );
			resourceDir.mkdirs();
			resourceDir.mkdir();
		}

		// Icons, Skins, Models
		// Get the classloader in order to load the images
		ClassLoader classloader = (net.minecraft.server.MinecraftServer.class).getClassLoader();
		Method method = null;
		try
		{
			method = (java.net.URLClassLoader.class).getDeclaredMethod( "addURL", java.net.URL.class );
			method.setAccessible( true );
		}
		catch( Exception e )
		{
			log( "Failed to get class loader. All content loading will now fail." );
			e.printStackTrace();
		}

		contentPacks = proxy.getContentList( method, classloader );
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		creativeTabs = new OB_CreativeTabs( "Obj block" );

		materialMap.put( "air", Material.air );
		materialMap.put( "grass", Material.grass );
		materialMap.put( "ground", Material.ground );
		materialMap.put( "wood", Material.wood );
		materialMap.put( "rock", Material.rock );
		materialMap.put( "iron", Material.iron );
		materialMap.put( "anvil", Material.anvil );
		materialMap.put( "water", Material.water );
		materialMap.put( "lava", Material.lava );
		materialMap.put( "leaves", Material.leaves );
		materialMap.put( "plants", Material.plants );
		materialMap.put( "vine", Material.vine );
		materialMap.put( "sponge", Material.sponge );
		materialMap.put( "cloth", Material.cloth );
		materialMap.put( "fire", Material.fire );
		materialMap.put( "sand", Material.sand );
		materialMap.put( "circuits", Material.circuits );
		materialMap.put( "carpet", Material.carpet );
		materialMap.put( "glass", Material.glass );
		materialMap.put( "redstoneLight", Material.redstoneLight );
		materialMap.put( "tnt", Material.tnt );
		materialMap.put( "coral", Material.coral );
		materialMap.put( "ice", Material.ice );
		materialMap.put( "packedIce", Material.packedIce );
		materialMap.put( "snow", Material.snow );
		materialMap.put( "craftedSnow", Material.craftedSnow );
		materialMap.put( "cactus", Material.cactus );
		materialMap.put( "clay", Material.clay );
		materialMap.put( "gourd", Material.gourd );
		materialMap.put( "dragonEgg", Material.dragonEgg );
		materialMap.put( "portal", Material.portal );
		materialMap.put( "cake", Material.cake );
		materialMap.put( "web", Material.web );

		GameRegistry.registerTileEntity( OB_TileEntity.class, "objblocktile" );
		proxy.registerRenderer();
		readBlockData( contentPacks );
		proxy.registerItemRenderer();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}

	private void readBlockData(List<File> contentPacks)
	{
		for( File contentPack : contentPacks )
		{
			if( contentPack.isDirectory() )
			{
				File typesDir = new File( contentPack, "/assets/objblock/blocks/" );
				if( typesDir.exists() )
				{
					for( File file : typesDir.listFiles( new FileFilter()
					{
						public boolean accept(File pathname)
						{
							return pathname.getName().toLowerCase().endsWith( ".json" );
						}
					} ) )
					{
						try
						{
							//	log( "[" + contentPack + "]" + file.getName() );
							FileInputStream inputStream = new FileInputStream( file );
							addBlock( file.getName(), inputStream );
							inputStream.close();
						}
						catch( Exception e )
						{
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				try
				{
					FileInputStream inputStream = new FileInputStream( contentPack );
					ZipInputStream zipIn = new ZipInputStream( inputStream );
					ZipEntry zipEntry;

					// zip内のファイルがなくなるまで読み続ける
					while( null != (zipEntry = zipIn.getNextEntry()) )
					{
						if( zipEntry.getName().toLowerCase().endsWith( ".json" ) )
						{
							addBlock( zipEntry.getName(), zipIn );
						}
						zipIn.closeEntry();
					}

					inputStream.close();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void addBlock(String name, InputStream inputstream)
	{
		name = name.substring( name.lastIndexOf( "/" ) + 1, name.length() - 5 );

		JsonObject jsonobject = null;
		try
		{
			JsonParser jsonparser = new JsonParser();
			jsonobject = jsonparser.parse( IOUtils.toString( inputstream, Charsets.UTF_8 ) ).getAsJsonObject();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		addBlock( name, jsonobject );
	}

	private void addBlock(String name, JsonObject jsonobject)
	{
		String mName = OB_JsonUtils.getString( jsonobject, "Material", "wood" );

		OB_Block block = new OB_Block( materialMap.containsKey( mName ) ? materialMap.get( mName ) : Material.wood );
		block.setCreativeTab( creativeTabs );
		block.setStepSound( Block.soundTypeMetal );
		block.setHardness( OB_JsonUtils.getFloat( jsonobject, "Hardness", 0.2F ) );
		block.setLightLevel( OB_JsonUtils.getFloat( jsonobject, "LightLevel", 0.0F ) );
		block.setBlockTextureName( OB_Core.MOD_ID + ":" + name );
		block.texture = new ResourceLocation( OB_Core.MOD_ID, "textures/blocks/" + name + ".png" );
		block.setBlockName( name );

		if(OB_JsonUtils.getBoolean( jsonobject, "Unbreakable", true ))
		{
			block.setBlockUnbreakable();
		}

		block.colorAlpha = OB_JsonUtils.getFloat( jsonobject, "ColorAlpha", 1.0F );
		block.colorRed   = OB_JsonUtils.getFloat( jsonobject, "ColorRed"  , 1.0F );
		block.colorGreen = OB_JsonUtils.getFloat( jsonobject, "ColorGreen", 1.0F );
		block.colorBlue  = OB_JsonUtils.getFloat( jsonobject, "ColorBlue" , 1.0F );

		String color = OB_JsonUtils.getString( jsonobject, "ColorARGB", "" );
		if(!color.isEmpty())
		{
			long c = Long.parseLong( color, 16 );
			block.colorAlpha = (float)((c >> 24) & 0xFF) / 0xFF;
			block.colorRed   = (float)((c >> 16) & 0xFF) / 0xFF;
			block.colorGreen = (float)((c >>  8) & 0xFF) / 0xFF;
			block.colorBlue  = (float)((c >>  0) & 0xFF) / 0xFF;
		}

		block.activatedSound		= OB_JsonUtils.getString( jsonobject, "ClickSound", "" );
		block.activatedSoundPitch	= OB_JsonUtils.getFloat(  jsonobject, "ClickSoundPitch", 1 );
		block.activatedSoundVolume	= OB_JsonUtils.getFloat(  jsonobject, "ClickSoundVolume", 1 );

		block.ladder				= OB_JsonUtils.getBoolean( jsonobject, "Ladder", false );

		block.collidedDamage		= OB_JsonUtils.getFloat(  jsonobject, "CollidedDamage", 0 );

		proxy.addModel( block, name );

		GameRegistry.registerBlock( block, name );

		objBlocks.add( block );
	}
}
