package org.silvercatcher.reforged.proxy;

import java.io.File;

import org.silvercatcher.reforged.*;
import org.silvercatcher.reforged.ReforgedReferences.GlobalValues;
import org.silvercatcher.reforged.api.ReforgedAdditions;
import org.silvercatcher.reforged.entities.*;
import org.silvercatcher.reforged.props.*;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public static final String[] sounds = new String[] { "boomerang_break", "boomerang_hit", "boomerang_throw",
			"crossbow_reload", "crossbow_shoot", "musket_shoot", "shotgun_reload", "shotgun_shoot" };

	// Items for Config
	public static boolean battleaxe, blowgun, boomerang, firerod, javelin, katana, knife, musket, nest_of_bees, sabre,
			keris, caltrop, dynamite, crossbow, pike, mace, dirk;

	// IDs
	public static int goalseekerid;

	public static final String items = "Items";

	public static SoundEvent getSound(String name) {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation(ReforgedMod.ID, name));
	}

	public void init(FMLInitializationEvent event) {
		ReforgedRegistry.registerRecipes();
	}

	private void loadConfig(FMLPreInitializationEvent e) {
		File configdir = new File(e.getModConfigurationDirectory(), ReforgedMod.NAME);
		File configfile = new File(configdir, "reforged.cfg");
		if (!configfile.exists())
			configdir.mkdirs();
		// Get an instance of Config
		Configuration config = new Configuration(configfile);

		// Load Config
		config.load();

		// Items
		battleaxe = config.getBoolean("Battleaxe", items, true, "Enable the Battleaxe");
		blowgun = config.getBoolean("Blowgun", items, true, "Enable the Blowgun plus Darts");
		boomerang = config.getBoolean("Boomerang", items, true, "Enable the Boomerang");
		firerod = config.getBoolean("Firerod", items, true, "Enable the Firerod");
		javelin = config.getBoolean("Javelin", items, true, "Enable the Javelin");
		katana = config.getBoolean("Katana", items, true, "Enable the Katana");
		knife = config.getBoolean("Knife", items, true, "Enable the Knife");
		musket = config.getBoolean("Musket", items, true, "Enable the Musket and Blunderbuss");
		nest_of_bees = config.getBoolean("Nest Of Bees", items, true, "Enable the Nest Of Bees");
		sabre = config.getBoolean("Sabre", items, true, "Enable the Sabre");
		keris = config.getBoolean("Kris", items, true, "Enable the Kris");
		caltrop = config.getBoolean("Caltrop", items, true, "Enable the Caltrop");
		dynamite = config.getBoolean("Dynamite", items, true, "Enable the Dynamite");
		crossbow = config.getBoolean("Crossbow", items, true, "Enable the Crossbow plus Bolt");
		pike = config.getBoolean("Pike", items, true, "Enable the Pike");
		mace = config.getBoolean("Mace", items, true, "Enable the Mace");
		dirk = config.getBoolean("Dirk", items, true, "Enable the Dirk");

		// IDs
		goalseekerid = config.getInt("Goalseeker", "IDs", 100, 0, 256,
				"This specifies the Enchantment ID of the Goalseeker-Enchantment");

		// Save config
		config.save();
	}

	public void postInit(FMLPostInitializationEvent event) {
		ReforgedMod.battlegearDetected = Loader.isModLoaded("battlegear2");
	}

	public void preInit(FMLPreInitializationEvent event) {
		loadConfig(event);
		ReforgedRegistry.registerEventHandler(new ReforgedEvents());
		ReforgedRegistry.registerEventHandler(new ReforgedMonsterArmourer());
		ReforgedRegistry.createItems();
		ReforgedRegistry.registerItems();
		ReforgedRegistry.registerPackets();
		Enchantment.REGISTRY.register(goalseekerid, new ResourceLocation(ReforgedMod.ID, "goalseeker"),
				ReforgedAdditions.goalseeker);
		CapabilityManager.INSTANCE.register(IStunProperty.class, new StorageStun(), DefaultStunImpl.class);
		for (String s : sounds) {
			ResourceLocation loc = new ResourceLocation(ReforgedMod.ID, s);
			GameRegistry.register(new SoundEvent(loc), loc);
		}
		registerEntities();
	}

	private void registerEntities() {

		if (GlobalValues.BOOMERANG)
			ReforgedRegistry.registerEntity(EntityBoomerang.class, "Boomerang");
		if (GlobalValues.JAVELIN)
			ReforgedRegistry.registerEntity(EntityJavelin.class, "Javelin");

		if (GlobalValues.MUSKET) {
			ReforgedRegistry.registerEntity(EntityBulletMusket.class, "BulletMusket");
			ReforgedRegistry.registerEntity(EntityBulletBlunderbuss.class, "BulletBlunderbuss");
		}

		if (GlobalValues.CROSSBOW)
			ReforgedRegistry.registerEntity(EntityCrossbowBolt.class, "CrossbowBolt");
		if (GlobalValues.BLOWGUN)
			ReforgedRegistry.registerEntity(EntityDart.class, "Dart");
		if (GlobalValues.CALTROP)
			GameRegistry.registerTileEntity(TileEntityCaltrop.class, "Caltrop");
		if (GlobalValues.DYNAMITE)
			ReforgedRegistry.registerEntity(EntityDynamite.class, "Dynamite");
	}

	protected void registerEntityRenderers() {
	}

	public void registerItemRenderer(Item item, int meta, String id) {
	}

	protected void registerItemRenderers() {
	}

}
