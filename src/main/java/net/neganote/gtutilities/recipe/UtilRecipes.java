package net.neganote.gtutilities.recipe;

import com.gregtechceu.gtceu.api.GTValues;

import com.gregtechceu.gtceu.common.data.GTItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.neganote.gtutilities.common.item.UtilItems;
import net.neganote.gtutilities.config.UtilConfig;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gregtechceu.gtceu.data.recipe.CraftingComponent.*;
import static net.minecraft.world.item.Items.PAPER;
import static net.neganote.gtutilities.common.item.UtilItems.OMNITOOL;
import static net.neganote.gtutilities.common.machine.UtilMachines.ENERGY_CONVERTER_64A;

public class UtilRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        if (UtilConfig.INSTANCE.features.converters64aEnabled) {
            register64AConverterRecipes(provider);
        }

        if (UtilConfig.INSTANCE.features.omnitoolEnabled) {
            registerOmnitoolRecipe(provider);
        }

        if (UtilConfig.INSTANCE.features.punchedCardsEnabled) {
            registerPunchCardRecipes(provider);
        }
    }

    private static ItemStack getPowerUnit(int tier) {
        return switch (tier) {
            case GTValues.LV -> GTItems.POWER_UNIT_LV.asStack();
            case GTValues.MV -> GTItems.POWER_UNIT_MV.asStack();
            case GTValues.HV -> GTItems.POWER_UNIT_HV.asStack();
            case GTValues.EV -> GTItems.POWER_UNIT_EV.asStack();
            case GTValues.IV -> GTItems.POWER_UNIT_IV.asStack();
            default -> throw new IllegalArgumentException("Invalid tier of power unit: " + tier);
        };
    }

    public static void registerOmnitoolRecipe(Consumer<FinishedRecipe> provider) {
        ASSEMBLER_RECIPES.recipeBuilder("omnitool")
                .inputItems(getPowerUnit(UtilConfig.INSTANCE.features.omnitoolTier))
                .inputItems(CIRCUIT.getIngredient(UtilConfig.INSTANCE.features.omnitoolTier), 2)
                .inputItems(EMITTER.getIngredient(UtilConfig.INSTANCE.features.omnitoolTier), 1)
                .inputItems(CABLE_QUAD.getIngredient(UtilConfig.INSTANCE.features.omnitoolTier), 3)
                .inputItems(MOTOR.getIngredient(UtilConfig.INSTANCE.features.omnitoolTier), 2)
                .outputItems(OMNITOOL)
                .EUt(GTValues.VEX[UtilConfig.INSTANCE.features.omnitoolTier]).duration(20 * 60)
                .save(provider);
    }

    public static void registerPunchCardRecipes(Consumer<FinishedRecipe> provider) {
        for (int i = 0; i < 16; i++) {
            PACKER_RECIPES.recipeBuilder("punch_card_" + i)
                    .inputItems(PAPER, 1)
                    .circuitMeta(i)
                    .outputItems(UtilItems.punchCard(8, i))
                    .EUt(8).duration(10)
                    .save(provider);
        }
    }

    public static void register64AConverterRecipes(Consumer<FinishedRecipe> provider) {
        for (int tier : GTValues.tiersBetween(GTValues.ULV, GTValues.UXV)) {
            ASSEMBLER_RECIPES.recipeBuilder("converter_64a_" + GTValues.VN[tier])
                    .inputItems(HULL.getIngredient(tier))
                    .inputItems(CIRCUIT.getIngredient(tier + 2))
                    .inputItems(CABLE_HEX.getIngredient(0), 4)
                    .inputItems(CABLE_HEX.getIngredient(tier), 16)
                    .outputItems(ENERGY_CONVERTER_64A[tier])
                    .EUt(GTValues.VEX[tier]).duration(40)
                    .save(provider);
        }
    }
}
