package com.ferreusveritas.dynamictrees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.compat.CommonProxyCompat;
import com.ferreusveritas.dynamictrees.proxy.CommonProxy;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;

import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
* <p><pre><tt><b>
*  ╭─────────────────╮
*  │                 │
*  │       ▓▓        │
*  │      ▓▓▓▓▓      │
*  │     ▓▓▓┼▓▓▓     │
*  │    ▓▓▓▓│▓▓▓     │
*  │    ▓▓└─┧ ▓▓▓    │
*  │     ▓  ┠─┘▓     │
*  │        ┃        │
*  │  █████████████  │
*  │  ▒▒▒▒▒▒▒▒▒▒▒▒▒  │
*  │  ░░░░░░░░░░░░░  │
*  ╞═════════════════╡
*  │  DYNAMIC TREES  │
*  ╰─────────────────╯
* </b></tt></pre></p>
* 
* <p>
* 2016-2017 Ferreusveritas
* </p>
*
*/
@Mod(modid = ModConstants.MODID, version=ModConstants.VERSION,dependencies="after:ComputerCraft;after:Quark")
public class DynamicTrees {

	@Mod.Instance(ModConstants.MODID)
	public static DynamicTrees instance;
	
	@SidedProxy(clientSide = "com.ferreusveritas.dynamictrees.proxy.ClientProxy", serverSide = "com.ferreusveritas.dynamictrees.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	//This will provide us with a proxy for dealing with compatibility with other mods
	@SidedProxy(clientSide = "com.ferreusveritas.dynamictrees.compat.ClientProxyCompat", serverSide = "com.ferreusveritas.dynamictrees.compat.CommonProxyCompat")
	public static CommonProxyCompat compatProxy;
	
	public static final CreativeTabs dynamicTreesTab = new CreativeTabs(ModConstants.MODID) {
        @SideOnly(Side.CLIENT)
		@Override
		public ItemStack getTabIconItem() {
			return TreeRegistry.findSpeciesSloppy(BlockPlanks.EnumType.OAK.getName()).getSeedStack(1);
		}
	};

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		try { //This is need because something silently absorbs exceptions outside of this event.
			ModConfigs.preInit(event);//Naturally this comes first so we can react to settings
			TreeGenerator.preInit();//Create the generator
			
			ModBlocks.preInit();
			ModItems.preInit();
			ModTrees.preInit();
			
			proxy.preInit();
			compatProxy.preInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RegistrationHandler.registerBlocks();
		RegistrationHandler.registerItems();
		proxy.registerModels();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		
		TreeGenerator.init();//This is run during the init phase to cache tree data that was created during the preInit phase
		
		RegistrationHandler.registerRecipes();
		
		proxy.init();
		compatProxy.init();
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		
		public static void registerBlocks() {
			ModBlocks.registerBlocks();	
		}
		
		public static void registerItems() {
			ModItems.registerItems();
		}
		
		public static void registerRecipes() {
			ModRecipes.registerRecipes();
		}
		
		public static void registerModels() {
			ModModels.registerModels();
		}

		@SubscribeEvent
		public static void newRegistry(RegistryEvent.NewRegistry event) {
			Species.newRegistry(event);
		}
		
	}
	
}