package net.neganote.gtutilities;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neganote.gtutilities.common.item.UtilItems;
import net.neganote.gtutilities.common.machine.UtilMachines;
import net.neganote.gtutilities.config.UtilConfig;
import net.neganote.gtutilities.data.UtilDatagen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neganote.gtutilities.recipe.IntPunchCardIngredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GregTechModernUtilities.MOD_ID)
public class GregTechModernUtilities {

    public static final String MOD_ID = "gtmutils";
    public static final Logger LOGGER = LogManager.getLogger();
    public static GTRegistrate REGISTRATE = GTRegistrate.create(GregTechModernUtilities.MOD_ID);

    public static RegistryEntry<CreativeModeTab> UTIL_CREATIVE_TAB = null;

    public GregTechModernUtilities() {
        GregTechModernUtilities.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::addMaterialRegistries);
        modEventBus.addListener(this::addMaterials);
        modEventBus.addListener(this::modifyMaterials);
        modEventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        modEventBus.addGenericListener(MachineDefinition.class, this::registerMachines);

        // Most other events are fired on Forge's bus.
        // If we want to use annotations to register event listeners,
        // we need to register our object like this!
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void init() {
        UtilConfig.init();
        if (UtilConfig.INSTANCE.features.omnitoolEnabled) {
            UTIL_CREATIVE_TAB = REGISTRATE
                    .defaultCreativeTab(GregTechModernUtilities.MOD_ID,
                            builder -> builder
                                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(
                                            GregTechModernUtilities.MOD_ID, REGISTRATE))
                                    .title(REGISTRATE.addLang("itemGroup", GregTechModernUtilities.id("creative_tab"),
                                            "GregTech Modern Utilities"))
                                    .icon(UtilItems.OMNITOOL::asStack)
                                    .build())
                    .register();
        } else {
            UTIL_CREATIVE_TAB = REGISTRATE
                    .defaultCreativeTab(GregTechModernUtilities.MOD_ID,
                            builder -> builder
                                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(
                                            GregTechModernUtilities.MOD_ID, REGISTRATE))
                                    .title(REGISTRATE.addLang("itemGroup", GregTechModernUtilities.id("creative_tab"),
                                            "GregTech Modern Utilities"))
                                    .icon(GTItems.INTEGRATED_CIRCUIT_HV::asStack)
                                    // same as above, but using a GTm item for the icon instead
                                    .build())
                    .register();
        }
        UtilItems.init();
        REGISTRATE.registerRegistrate();
        UtilDatagen.init();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CraftingHelper.register(IntPunchCardIngredient.TYPE, IntPunchCardIngredient.SERIALIZER);
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Hey, we're on Minecraft version {}!", Minecraft.getInstance().getLaunchedVersion());
    }

    // You MUST have this for custom materials.
    // Remember to register them not to GT's namespace, but your own.
    private void addMaterialRegistries(MaterialRegistryEvent event) {
        GTCEuAPI.materialManager.createRegistry(GregTechModernUtilities.MOD_ID);
    }

    // As well as this.
    private void addMaterials(MaterialEvent event) {
        // CustomMaterials.init();
    }

    // This is optional, though.
    private void modifyMaterials(PostMaterialEvent event) {
        // CustomMaterials.modify();
    }

    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        // CustomRecipeTypes.init();
    }

    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        UtilMachines.init();
    }


}
