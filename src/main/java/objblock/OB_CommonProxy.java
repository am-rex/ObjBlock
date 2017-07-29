package objblock;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.server.MinecraftServer;

public class OB_CommonProxy
{
	protected static Pattern zipJar = Pattern.compile("(.+).(zip|jar)$");

	public void registerRenderer(){}
	public void registerItemRenderer(){}
	public void addModel(OB_Block block, String name){}

	public List<File> getContentList(Method method, ClassLoader classloader)
	{
		List<File> contentPacks = new ArrayList<File>();
		for (File file : OB_Core.resourceDir.listFiles())
		{
			//Load folders and valid zip files
			if( file.isDirectory() || zipJar.matcher(file.getName()).matches() )
			{
				//Add the directory to the content pack list
				OB_Core.log("Loaded content pack : " + file.getName());
				contentPacks.add(file);
			}
		}

		OB_Core.log("Loaded content pack list server side.");
		return contentPacks;
	}

	public File getRootDir()
	{
		return MinecraftServer.getServer().getFile( "" );
	}

	public String getSideString()
	{
		return "Server";
	}
}
